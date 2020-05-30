# About

This is a sample implementation of how to use Debezium. 
Debezium is a Kafka connect to capture the data changes of the database.
You can use this as a starting point.

# Flow

Data flows of this sample implementation.

```
#######        ############       #########       ##############       #######
# DB1 # ---->  # Debezium # ----> # Kafka # ----> # Sample App # ----> # DB2 #    
#######        ############       #########       ##############       #######
```

# Instruction

```shell
sh setup-network.sh
cd db
docker-compose up -d
cd ..
cd cluster
docker-compose up -d
sh init.sh
cd ..
cd sample-app
./gradlew bootRun
```

And then insert, update, delete on the Db1. You should see every data changes applied to the Db2.

Using Debezium, You can do what replication can't do.

* Impossible replication due to version mis-matching.
* Impossible replication due to database mis-matching.
* Denormalization.
* Sharding.

# References

1. [MySQL CDC With Debezium #1](https://m0rph2us.github.io/mysql/cdc/debezium/2020/05/23/mysql-cdc-with-debezium-1.html)