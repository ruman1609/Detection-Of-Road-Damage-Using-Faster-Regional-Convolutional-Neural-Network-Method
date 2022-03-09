package com.rudyrachman16.skripsi.ui.menu

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.rudyrachman16.skripsi.core.IRepository

class MenuViewModel(private val repository: IRepository) {
    fun getTargetUri() = repository.getTargetUri()
    fun getCameraIntent(launcher: ActivityResultLauncher<Intent>) =
        repository.createTakePictureIntent(launcher)

    fun savePictureQ(): String = repository.savePictureQ()
}