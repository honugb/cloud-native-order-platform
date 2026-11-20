terraform {
  required_version = ">= 1.6.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
  default_tags {
    tags = {
      Project     = "cloud-native-order-platform"
      Environment = "dev"
      ManagedBy   = "Terraform"
      Owner       = "devops-lab"
    }
  }
}
