data "aws_availability_zones" "available" {
  state = "available"
}

locals {
  name = "${var.project}-${var.environment}"
  azs  = slice(data.aws_availability_zones.available.names, 0, 2)
}

module "network" {
  source                = "../../modules/network"
  name                  = local.name
  vpc_cidr              = var.vpc_cidr
  availability_zones    = local.azs
  create_nat_gateway    = var.enable_eks
  cluster_name          = "${local.name}-eks"
}

module "ecr" {
  source          = "../../modules/ecr"
  repository_name = "${local.name}-order-service"
  force_delete    = true # Lab teardown: permits deleting nonempty repository.
}

module "iam" {
  source = "../../modules/iam"
  count  = var.enable_eks ? 1 : 0
  name   = local.name
}

module "eks" {
  source                  = "../../modules/eks"
  count                   = var.enable_eks ? 1 : 0
  name                    = "${local.name}-eks"
  kubernetes_version      = var.kubernetes_version
  private_subnet_ids      = module.network.private_subnet_ids
  cluster_role_arn        = module.iam[0].cluster_role_arn
  node_role_arn           = module.iam[0].node_role_arn
  node_role_policy_arns   = module.iam[0].node_role_policy_arns
  public_access_cidrs     = var.eks_public_access_cidrs
  node_instance_types     = var.node_instance_types
  node_desired_size       = var.node_desired_size
  depends_on              = [module.network, module.iam]
}
