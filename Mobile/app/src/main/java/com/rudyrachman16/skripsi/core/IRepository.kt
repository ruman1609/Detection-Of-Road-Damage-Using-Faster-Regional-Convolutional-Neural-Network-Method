package com.rudyrachman16.skripsi.core

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher

interface IRepository {
    fun createTakePictureIntent(launcher: ActivityResultLauncher<Intent>)
    fun savePictureQ(): String
    fun getTargetUri(): Uri?
}