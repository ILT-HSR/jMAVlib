package me.drton.jmavlib.mavlink

import android.content.Context
import android.util.Log

private val TAG = MAVLinkSchemaRegistry::class.simpleName

class MAVLinkSchemaRegistry private constructor() {

    private object InstanceHolder { val INSTANCE = MAVLinkSchemaRegistry() }

    companion object {
        val instance: MAVLinkSchemaRegistry by lazy { InstanceHolder.INSTANCE }
    }

    private val fSchemas = HashMap<String, MAVLinkSchema>()

    operator fun set(context: Context, name: String, filename: String): MAVLinkSchema? {
        return when(this[name]) {
            null -> doRegister(context, name, filename)
            else -> this[name]
        }
    }

    operator fun get(name: String) = fSchemas[name]

    val schemas get() = fSchemas.keys

    private fun doRegister(context: Context, name: String, filename: String): MAVLinkSchema? {
        return try {
            val schema = MAVLinkSchema(context, filename)
            fSchemas[name] = schema
            schema
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register schema '$name': ", e)
            null
        }
    }

}