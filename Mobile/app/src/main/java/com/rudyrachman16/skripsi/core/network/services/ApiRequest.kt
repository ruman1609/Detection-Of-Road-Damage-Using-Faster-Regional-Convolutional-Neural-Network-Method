package com.rudyrachman16.skripsi.core.network.services

import com.rudyrachman16.skripsi.core.network.model.PredictionResultRes
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiRequest {
    @Multipart
    @POST("/predict")
    suspend fun postPredictImage(@Part image: MultipartBody.Part): PredictionResultRes

    @GET("/predict")
    suspend fun getPrediction(): PredictionResultRes
}