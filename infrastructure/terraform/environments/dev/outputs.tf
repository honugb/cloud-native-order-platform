output "vpc_id" { value = module.network.vpc_id }
output "public_subnet_ids" { value = module.network.public_subnet_ids }
output "private_subnet_ids" { value = module.network.private_subnet_ids }
output "ecr_repository_url" { value = module.ecr.repository_url }
output "eks_cluster_name" { value = var.enable_eks ? module.eks[0].cluster_name : null }
output "eks_cluster_endpoint" { value = var.enable_eks ? module.eks[0].cluster_endpoint : null }
