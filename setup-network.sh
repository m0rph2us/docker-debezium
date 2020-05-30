#!/bin/bash

# Set alias to the lo interface
# sudo ifconfig lo0 alias 10.200.10.1/24

docker network create --gateway 172.255.0.1 --subnet 172.255.0.0/16 debezium-docker-net