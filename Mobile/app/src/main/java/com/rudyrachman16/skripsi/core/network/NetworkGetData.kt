package com.rudyrachman16.skripsi.core.network

import android.util.Log
import com.rudyrachman16.skripsi.core.network.model.PredictionResultRes
import com.rudyrachman16.skripsi.core.network.services.ApiRequest
import com.rudyrachman16.skripsi.core.network.services.ApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class NetworkGetData(private val api: ApiRequest) {

    companion object {
        private var INSTANCE: NetworkGetData? = null

        fun getInstance(api: ApiRequest) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: NetworkGetData(api).apply { INSTANCE = this }
        }
    }

    fun postPredictImage(image: File): Flow<ApiStatus<PredictionResultRes>> = flow {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), image)
        val imagePart = MultipartBody.Part.createFormData("image", image.name, reqFile)
        api.postPredictImage(imagePart).apply {
            if (id == null || this.image == null) emit(ApiStatus.Empty)
            else emit(ApiStatus.Success(PredictionResultRes(this.image, id, null)))
        }
    }.catch { e ->
        Log.e("NetworkGetData", e.message ?: "")
        e.printStackTrace()
        emit(ApiStatus.Failed(e as Exception))
    }.flowOn(Dispatchers.IO)

    fun getPrediction(): Flow<ApiStatus<PredictionResultRes>> = flow {
        api.getPrediction().apply {
            if (id == null || this.image == null || objects == null) emit(ApiStatus.Empty)
            else emit(ApiStatus.Success(PredictionResultRes(image, id, objects)))
        }
    }.catch { e ->
        Log.e("NetworkGetData", e.message ?: "")
        e.printStackTrace()
        emit(ApiStatus.Failed(e as Exception))
    }.flowOn(Dispatchers.IO)
}