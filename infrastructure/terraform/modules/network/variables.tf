variable "name" { type = string }
variable "vpc_cidr" { type = string }
variable "availability_zones" { type = list(string) }
variable "create_nat_gateway" { type = bool }
variable "cluster_name" { type = string }
