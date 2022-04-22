package com.rudyrachman16.skripsi.core.network.services

import com.rudyrachman16.skripsi.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiCall {
    private val client = OkHttpClient.Builder().apply {
        readTimeout(2, TimeUnit.MINUTES)
        connectTimeout(2, TimeUnit.MINUTES)
        callTimeout(2, TimeUnit.MINUTES)
        writeTimeout(2, TimeUnit.MINUTES)
    }.build()

    val api: ApiRequest by lazy {
        Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            baseUrl(BuildConfig.BASE_URL)
            client(client)
        }.build().create(ApiRequest::class.java)
    }
}