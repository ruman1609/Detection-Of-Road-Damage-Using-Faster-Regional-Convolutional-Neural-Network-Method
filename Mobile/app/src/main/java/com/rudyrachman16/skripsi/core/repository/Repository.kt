package com.rudyrachman16.skripsi.core.repository

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import com.rudyrachman16.skripsi.core.MapVal
import com.rudyrachman16.skripsi.core.Status
import com.rudyrachman16.skripsi.core.model.PredictionResult
import com.rudyrachman16.skripsi.core.network.NetworkGetData
import com.rudyrachman16.skripsi.core.network.services.ApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Repository private constructor(
    private val context: Context,
    private val api: NetworkGetData
) : IRepository {

    companion object {
        @Volatile
        private var INSTANCE: IRepository? = null

        fun getInstance(context: Context, api: NetworkGetData) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Repository(context, api).apply { INSTANCE = this }
        }
    }

    private var currentPath = ""
    private var targetUri: Uri? = null

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timestamp, ".jpg", storageDir)
            .apply { currentPath = absolutePath }
    }

    override fun savePictureQ(): String {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val f = File(currentPath)
            val cr = context.contentResolver
            val bitmap = BitmapFactory.decodeFile(currentPath)
            val path = "${Environment.DIRECTORY_PICTURES}${File.separator}PoCkDetection"
            val values = createContentValues(f.name, path)
            var uri: Uri? = null
            try {
                uri = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
                val os = cr.openOutputStream(uri)
                try {
                    val result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                    if (!result) throw Exception()
                } catch (e: Exception) {
                    e.printStackTrace()
                    throw e
                } finally {
                    os?.close()
                    targetUri = uri
                }
                f.delete()
                if (f.exists()) {
                    f.canonicalFile.delete()
                    if (f.exists()) return f.name
                }
            } catch (e: Exception) {
                e.printStackTrace()
                uri?.let {
                    cr.delete(it, null, null)
                }
            }
        }
        return ""
    }

    override fun getTargetUri(): Uri? = targetUri

    private fun createContentValues(title: String, path: String): ContentValues =
        ContentValues().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.TITLE, "$title.jpg")
                put(MediaStore.MediaColumns.DISPLAY_NAME, "$title.jpg")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis())
                put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis())
                put(MediaStore.MediaColumns.RELATIVE_PATH, path)
            } else {
                put(MediaStore.Images.Media.TITLE, "$title.jpg")
                put(MediaStore.Images.Media.DISPLAY_NAME, "$title.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            }
        }

    override fun createTakePictureIntent(launcher: ActivityResultLauncher<Intent>) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(context.packageManager).also {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (e: IOException) {
                        e.printStackTrace()
                        null
                    }
                    photoFile?.also {
                        val photoURI =
                            FileProvider.getUriForFile(context, "com.rudyrachman16.skripsi", it)
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        launcher.launch(takePictureIntent)
                    }
                } else {
                    val timestamp =
                        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                    val path = "${Environment.DIRECTORY_PICTURES}${File.separator}PoCkDetection"
                    val values = createContentValues(timestamp, path)
                    val photoURI = context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                    )
                    targetUri = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    launcher.launch(takePictureIntent)
                }
            }
        }
    }

    override fun postPrediction(image: File): Flow<Status<PredictionResult>> = flow {
        emit(Status.Loading)
        when (val response = api.postPredictImage(image).first()) {
            is ApiStatus.Success -> emit(Status.Success(MapVal.predFromResToMod(response.data)))
            is ApiStatus.Empty -> emit(Status.Success(PredictionResult(-1, "", listOf())))
            is ApiStatus.Failed -> emit(Status.Error(response.error))
        }
    }.flowOn(Dispatchers.IO)


    override fun getPrediction(): Flow<Status<PredictionResult>> = flow {
        emit(Status.Loading)
        when (val response = api.getPrediction().first()) {
            is ApiStatus.Success -> emit(Status.Success(MapVal.predFromResToMod(response.data)))
            is ApiStatus.Empty -> emit(Status.Success(PredictionResult(-1, "", listOf())))
            is ApiStatus.Failed -> emit(Status.Error(response.error))
        }
    }.flowOn(Dispatchers.IO)
}