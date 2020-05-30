#!/bin/bash

docker-compose exec kafka-1 /kafka/bin/kafka-topics.sh \
  --create --bootstrap-server kafka-1:9092 \
  --replication-factor 1 --partitions 1 --topic cdc_db1

docker-compose exec kafka-1 /kafka/bin/kafka-topics.sh \
  --create --bootstrap-server kafka-1:9092 \
  --replication-factor 1 --partitions 1 --topic cdc_db1.sample.tb_user

curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" \
  http://127.0.0.1:8083/connectors/ -d @register-cdc-db1.json \
  && curl http://127.0.0.1:8083/connectors/mysql-connector-db1/status