package com.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.EnableKafkaStreams

@EnableKafka
@EnableKafkaStreams
@SpringBootApplication
class KfkappApplication

fun main(args: Array<String>) {
    runApplication<KfkappApplication>(*args)
}
