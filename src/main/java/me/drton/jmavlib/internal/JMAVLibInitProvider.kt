package me.drton.jmavlib.internal

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import me.drton.jmavlib.mavlink.MAVLinkSchemaRegistry

private val TAG = JMAVLibInitProvider::class.simpleName
private const val SCHEMAS = "schemas"

class JMAVLibInitProvider : ContentProvider() {

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? = null

    override fun onCreate(): Boolean {

        context.assets.list(SCHEMAS).forEach {
            val filenameParts = it.split(".")
            when (filenameParts.size) {
                2 -> {
                    MAVLinkSchemaRegistry[context, filenameParts[0]] = "$SCHEMAS/$it"
                }
                else -> Log.e(TAG, "Skipping invalid schema file '$SCHEMAS/$it'")
            }
        }

        return true
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun getType(uri: Uri?): String? = null

    override fun insert(uri: Uri?, values: ContentValues?): Uri? = null

}