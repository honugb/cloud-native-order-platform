variable "aws_region" {
  type    = string
  default = "ap-south-1"
}
variable "project" {
  type    = string
  default = "cnop"
}
variable "environment" {
  type    = string
  default = "dev"
}
variable "vpc_cidr" {
  type    = string
  default = "10.42.0.0/16"
}
variable "enable_eks" {
  description = "Create costly NAT gateway, EKS control plane and workers only when true."
  type        = bool
  default     = false
}
variable "kubernetes_version" {
  type    = string
  default = "1.37"
}
variable "eks_public_access_cidrs" {
  description = "Trusted admin public IP CIDRs, e.g. ["203.0.113.10/32"]. Required when enabling EKS."
  type        = list(string)
  default     = []
}
variable "node_instance_types" {
  type    = list(string)
  default = ["t3.medium"]
}
variable "node_desired_size" {
  type    = number
  default = 1
}
