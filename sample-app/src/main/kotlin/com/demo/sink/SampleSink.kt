package com.demo.sink

import com.demo.config.db.Db2
import org.apache.avro.generic.GenericRecord
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import javax.persistence.EntityManagerFactory

@Component
class SampleSink(
        @Qualifier(Db2.ENTITY_MANAGER)
        val emfDb2: EntityManagerFactory
) {

    @KafkaListener(
            topics = ["cdc_db1.sample.tb_user"],
            clientIdPrefix = "tb-user-updated"
    )
    fun consumeAndSink(
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) key: GenericRecord,
            @Payload(required = false) value: GenericRecord?
    ) {
        // To avoid tombstone message
        if (value == null) return

        //println("key : $key")
        //println("value : $value")

        val accessor = GenericCdcEventDataAccessor(key, value)

        when (accessor.op()) {
            "d" -> {
                emfDb2.createEntityManager().run {
                    try {
                        transaction.begin().let {
                            val query = createNativeQuery("""
                                DELETE FROM tb_user WHERE id = :id
                            """.trimIndent()
                            ).apply {
                                accessor.baseValues().also {
                                    setParameter("id", it["id"].toString().toBigInteger())
                                }
                            }

                            query.executeUpdate()

                            transaction.commit()
                        }
                    } finally {
                        close()
                    }
                }
            }
            "c", "u" -> {
                emfDb2.createEntityManager().run {
                    try {
                        transaction.begin().let {
                            val query = createNativeQuery("""
                                INSERT INTO tb_user (
                                    id,
                                    user_id,
                                    name,
                                    email,
                                    status,
                                    delete_yn,
                                    reg_dt,
                                    chg_dt
                                ) VALUES (
                                    :id,
                                    :user_id,
                                    :name,
                                    :email,
                                    :status,
                                    :delete_yn,
                                    :reg_dt,
                                    :chg_dt
                                )
                                ON DUPLICATE KEY UPDATE
                                    user_id = :user_id,
                                    name = :name,
                                    status = :status,
                                    delete_yn = :delete_yn,
                                    email = :email,
                                    chg_dt = :chg_dt
                            """.trimIndent()
                            ).apply {
                                accessor.baseValues().also {
                                    setParameter("id", it["id"].toString().toBigInteger())
                                    setParameter("user_id", it["user_id"].toString())
                                    setParameter("name", it["name"].toString())
                                    setParameter("email", it["email"].toString())
                                    setParameter("status", it["status"])
                                    setParameter("delete_yn", it["delete_yn"].toString())
                                    setParameter("reg_dt", LocalDateTime.now())
                                    setParameter("chg_dt", LocalDateTime.now())
                                }
                            }

                            query.executeUpdate()

                            transaction.commit()
                        }
                    } finally {
                        close()
                    }
                }
            }
        }
    }

}