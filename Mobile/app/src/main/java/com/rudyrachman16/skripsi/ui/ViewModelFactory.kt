package com.rudyrachman16.skripsi.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rudyrachman16.skripsi.Injection
import com.rudyrachman16.skripsi.core.repository.IRepository
import com.rudyrachman16.skripsi.ui.menu.MenuViewModel
import com.rudyrachman16.skripsi.ui.result.ResultViewModel

class ViewModelFactory private constructor(private val repository: IRepository) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ViewModelFactory(Injection.provideRepository(context)).apply {
                INSTANCE = this
            }
        }
    }

    @Suppress("UNCHECKED_CAST", "WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(MenuViewModel::class.java) -> {
            MenuViewModel(repository) as T
        }
        modelClass.isAssignableFrom(ResultViewModel::class.java) -> {
            ResultViewModel(repository) as T
        }
        else -> throw(Throwable("Unknown ViewModel class: ${modelClass.name}"))
    }
}