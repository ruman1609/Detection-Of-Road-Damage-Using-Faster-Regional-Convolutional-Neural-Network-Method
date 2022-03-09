package com.rudyrachman16.skripsi

import android.content.Context
import com.rudyrachman16.skripsi.core.Repository

object Injection {
    fun provideRepository(context: Context) = Repository.getInstance(context)
}