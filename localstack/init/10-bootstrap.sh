#!/usr/bin/env bash
set -euo pipefail

awslocal sqs create-queue --queue-name orders_request
awslocal sqs create-queue --queue-name orders_reply

awslocal dynamodb create-table \
  --table-name OrdersTable \
  --attribute-definitions \
      AttributeName=userId,AttributeType=S \
      AttributeName=orderId,AttributeType=S \
  --key-schema \
      AttributeName=userId,KeyType=HASH \
      AttributeName=orderId,KeyType=RANGE \
  --billing-mode PAY_PER_REQUEST