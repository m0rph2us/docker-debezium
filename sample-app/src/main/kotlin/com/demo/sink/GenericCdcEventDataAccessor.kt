package com.demo.sink

import org.apache.avro.generic.GenericRecord

interface CdcEventDataAccessor {

    fun keys(): Map<String, Any?>

    fun beforeValues(): Map<String, Any?>?

    fun afterValues(): Map<String, Any?>?

    fun baseValues(): Map<String, Any?>

    fun changes(): Map<String, Any?>

    fun op(): String

    fun tsMs(): Long

}

class GenericCdcEventDataAccessor(
        private val key: GenericRecord,
        private val value: GenericRecord
) : CdcEventDataAccessor {

    private val keys by lazy { key.toMap() }
    private val beforeValues by lazy { before()?.toMap() }
    private val afterValues by lazy { after()?.toMap() }
    private val changes by lazy { changesEither() }

    override fun keys() = keys

    override fun beforeValues() = beforeValues

    override fun afterValues() = afterValues

    override fun baseValues(): Map<String, Any?> {
        return beforeValues?.let {
            afterValues?.let {
                afterValues
            } ?: beforeValues
        } ?: afterValues!!
    }

    override fun changes() = changes

    fun before(): GenericRecord? = value.get("before") as GenericRecord?

    fun after(): GenericRecord? = value.get("after") as GenericRecord?

    fun changesEither(): Map<String, Any?> {
        val before = before()
        val after = after()

        return before?.let {
            after?.let {
                changesMutual(before, after)
            } ?: before.toMap()
        } ?: after!!.toMap()
    }

    fun changesMutual(before: GenericRecord, after: GenericRecord): Map<String, Any?> {
        return mutableMapOf<String, Any?>().also { result ->
            after.schema.fields.forEach { field ->
                after.get(field.name())?.also { afterValue ->
                    if (afterValue != before[field.name()]) result[field.name()] = afterValue
                }
            }
        }
    }

    fun GenericRecord.toMap(): Map<String, Any?> {
        return mutableMapOf<String, Any?>().also { result ->
            this.schema.fields.forEach { field ->
                result[field.name()] = this.get(field.name())
            }
        }
    }

    override fun op() = value.get("op")?.toString() ?: ""

    override fun tsMs() = value.get("ts_ms") as Long? ?: 0

}