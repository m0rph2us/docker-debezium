#!/bin/bash

set -e

mysql_sqls=(
    "init.sql"
    )

db_sqls=(
    "sample/init.sql"
    "sample/init-data.sql"
     )

sqlExecute() {
	path=$1
	shift
	sqls=("${@}")

	for file in "${sqls[@]}"
	do
		echo "- import: /$path/$file"
		mysql --default-character-set=utf8 -uroot -p${MYSQL_ROOT_PASSWORD} < "/$path/$file"
	done
}

sqlExecute "init-mysql" "${mysql_sqls[@]}"
sqlExecute "init-db" "${db_sqls[@]}"
