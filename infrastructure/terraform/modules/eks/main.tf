resource "aws_eks_cluster" "this" {
  name     = var.name
  role_arn = var.cluster_role_arn
  version  = var.kubernetes_version

  vpc_config {
    subnet_ids              = var.private_subnet_ids
    endpoint_private_access = true
    endpoint_public_access  = true
    public_access_cidrs     = var.public_access_cidrs
  }
  access_config {
    authentication_mode                         = "API"
    bootstrap_cluster_creator_admin_permissions = true # Lab only; production: explicit access entries.
  }
  upgrade_policy { support_type = "STANDARD" }

  lifecycle {
    precondition {
      condition     = length(var.public_access_cidrs) > 0 && alltrue([for cidr in var.public_access_cidrs : can(cidrhost(cidr, 0)) && cidr != "0.0.0.0/0"])
      error_message = "Set at least one valid restricted EKS public API CIDR (e.g. your IP/32); 0.0.0.0/0 is not allowed in this lab."
    }
  }
}

resource "aws_eks_node_group" "this" {
  cluster_name    = aws_eks_cluster.this.name
  node_group_name = "${var.name}-workers"
  node_role_arn   = var.node_role_arn
  subnet_ids      = var.private_subnet_ids
  instance_types  = var.node_instance_types
  capacity_type   = "ON_DEMAND"
  disk_size       = 30
  scaling_config {
    desired_size = var.node_desired_size
    min_size     = 1
    max_size     = 2
  }
  update_config { max_unavailable = 1 }

  # Force IAM policy attachments to exist before creating EC2 workers.
  depends_on = [aws_eks_cluster.this]
}
