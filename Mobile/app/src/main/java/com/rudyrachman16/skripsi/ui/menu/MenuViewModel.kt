package com.rudyrachman16.skripsi.ui.menu

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rudyrachman16.skripsi.core.repository.IRepository
import java.io.File

class MenuViewModel(private val repository: IRepository): ViewModel() {
    fun getTargetUri() = repository.getTargetUri()
    fun getCameraIntent(launcher: ActivityResultLauncher<Intent>) =
        repository.createTakePictureIntent(launcher)

    fun savePictureQ(): String = repository.savePictureQ()

    fun postPrediction(image: File) = repository.postPrediction(image).asLiveData()
}