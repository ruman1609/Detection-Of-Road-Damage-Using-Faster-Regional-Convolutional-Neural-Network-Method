package com.rudyrachman16.skripsi.core.repository

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import com.rudyrachman16.skripsi.core.Status
import com.rudyrachman16.skripsi.core.model.PredictionResult
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IRepository {
    fun createTakePictureIntent(launcher: ActivityResultLauncher<Intent>)
    fun savePictureQ(): String
    fun getTargetUri(): Uri?

    // Network
    fun postPrediction(image: File): Flow<Status<PredictionResult>>
    fun getPrediction(): Flow<Status<PredictionResult>>
}