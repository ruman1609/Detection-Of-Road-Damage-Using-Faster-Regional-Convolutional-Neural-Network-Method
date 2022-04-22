package com.rudyrachman16.skripsi

import android.content.Context
import com.rudyrachman16.skripsi.core.network.NetworkGetData
import com.rudyrachman16.skripsi.core.network.services.ApiCall
import com.rudyrachman16.skripsi.core.repository.Repository

object Injection {
    fun provideRepository(context: Context) =
        Repository.getInstance(context, NetworkGetData.getInstance(ApiCall.api))
}