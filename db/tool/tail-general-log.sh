#!/bin/bash

case $1 in
slave)
mysql_host="mysql-slave"
;;
*)
mysql_host="mysql-master"
;;
esac

container_id=$(docker-compose exec ${mysql_host} hostname | tr -d '\r')
docker-compose exec ${mysql_host} tail -f /var/lib/mysql/${container_id}.log
