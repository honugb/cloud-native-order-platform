output "cluster_role_arn" { value = aws_iam_role.cluster.arn }
output "node_role_arn" { value = aws_iam_role.nodes.arn }
output "node_role_policy_arns" { value = [for attachment in aws_iam_role_policy_attachment.nodes : attachment.policy_arn] }
