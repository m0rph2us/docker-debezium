package com.demo.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.streams.StreamsConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.StreamsBuilderFactoryBeanCustomizer

@Configuration
class KafkaStreamsConfig {

    @Value("\${spring.kafka.streams.num-threads}")
    val numThreads: Int = 1

    @Value("\${spring.kafka.consumer.auto-offset-reset}")
    val autoOffsetReset: String = "latest"

    @Bean
    fun kafkaStreamsCustomizer(): StreamsBuilderFactoryBeanCustomizer {
        return StreamsBuilderFactoryBeanCustomizer { streamsBuilderFactory ->
            streamsBuilderFactory.streamsConfiguration?.apply {
                put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, numThreads)
                put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset)
            }
        }
    }

}