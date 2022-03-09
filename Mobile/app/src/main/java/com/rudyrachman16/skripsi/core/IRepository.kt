package com.rudyrachman16.skripsi.core

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri

interface IRepository {
    fun savePicture(cr: ContentResolver, bitmap: Bitmap, uri: Uri): Boolean
}