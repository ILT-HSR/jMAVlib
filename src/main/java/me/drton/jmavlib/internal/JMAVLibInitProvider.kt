package me.drton.jmavlib.internal

import android.content.ContentProvider
import android.content.ContentValues
import android.content.res.AssetManager
import android.database.Cursor
import android.net.Uri
import me.drton.jmavlib.MAVLINK_SCHEMA_COMMON
import me.drton.jmavlib.mavlink.MAVLinkSchema

class JMAVLibInitProvider() : ContentProvider() {

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? = null

    override fun onCreate(): Boolean {
        MAVLINK_SCHEMA_COMMON = MAVLinkSchema(context.assets.open("common.xml", AssetManager.ACCESS_STREAMING))
        return true
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun getType(uri: Uri?): String? = null

    override fun insert(uri: Uri?, values: ContentValues?): Uri? = null

}