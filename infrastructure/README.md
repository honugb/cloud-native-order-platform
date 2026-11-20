# Phase 3 — AWS Terraform foundation

Lab-first AWS infrastructure for `cloud-native-order-platform`.

## What gets created
- By default: two-AZ VPC, two public and two private subnets, IGW and route tables, ECR repository. **No NAT gateway or EKS cluster**.
- With `enable_eks = true`: single NAT gateway and Elastic IP, EKS 1.37 standard-support cluster, IAM roles, one managed EC2 node group in private subnets.

**Warning:** Turning EKS on starts hourly charges. EKS standard-support control plane alone is $0.10/hour, plus EC2 nodes, NAT, public IPv4, disks, requests and networking. A single NAT is a lab compromise and single point of failure; production would use NAT per AZ or designed VPC endpoints, hardened access, and centralized remote state.

## Prerequisites
Terraform >=1.6, AWS CLI v2, kubectl compatible with EKS 1.37, AWS credentials with provisioning permissions and appropriate EC2/EKS quotas. No root-user access keys.

```powershell
aws sso login --profile devops-lab
aws sts get-caller-identity --profile devops-lab
$env:AWS_PROFILE = 'devops-lab'
cd infrastructure/terraform/environments/dev
Copy-Item terraform.tfvars.example terraform.tfvars
# Edit terraform.tfvars: set your public IP /32 to access the EKS API.
terraform init
terraform fmt -recursive
terraform validate
terraform plan -out=tfplan
terraform apply tfplan
terraform output
```

Start by leaving `enable_eks = false`; verify VPC and ECR. For EKS, set `enable_eks = true`, set `eks_public_access_cidrs` to **your** IP/32 (not `0.0.0.0/0`), and apply a new plan.

```powershell
terraform plan -out=tfplan
terraform apply tfplan
aws eks update-kubeconfig --region ap-south-1 --name cnop-dev-eks --profile devops-lab
kubectl get nodes -o wide
kubectl get pods -A
```

### Publish Docker Hub image into ECR (manual test)

```powershell
$region = 'ap-south-1'
$acct = aws sts get-caller-identity --query Account --output text
$ecrUri = terraform output -raw ecr_repository_url
aws ecr get-login-password --region $region | docker login --username AWS --password-stdin "$acct.dkr.ecr.$region.amazonaws.com"
docker pull 28312/order-service:latest
docker tag 28312/order-service:latest "${ecrUri}:manual-test"
docker push "${ecrUri}:manual-test"
```

For Phase 4, use GitHub Actions OIDC -> IAM role to push commit-SHA images to ECR instead of persistent AWS keys. The existing GitHub Actions Docker Hub publishing can continue unchanged until then.

### Teardown
First remove any Kubernetes `Service` of type `LoadBalancer`, Ingress provisioned ALBs, persistent volumes, or other out-of-band AWS resources. Do not delete state while AWS resources still exist.

```powershell
terraform plan -destroy -out=destroy.tfplan
terraform apply destroy.tfplan
```

Or `terraform destroy` from the **same** working directory/state and AWS account/region. Confirm empty ECR (lab sets `force_delete=true`), deleted EKS/node group, NAT, EIP and VPC. Inspect AWS Billing afterwards for stragglers. Never destroy another environment's state.

### State and security
The starter uses **local Terraform state** solely for the initial hands-on lab, and ignores it via `.gitignore`; it is not a production state strategy. Before multi-person or GitHub CI applies, bootstrap S3 state with versioning, encryption, tightly scoped IAM and S3 native locking (`use_lockfile = true`), then migrate state. Keep `terraform.tfvars`, state, plans and cloud credentials out of Git. Never put Atlas DB secrets in Terraform variables/state.
