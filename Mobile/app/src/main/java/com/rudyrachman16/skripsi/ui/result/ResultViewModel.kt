package com.rudyrachman16.skripsi.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rudyrachman16.skripsi.core.repository.IRepository

class ResultViewModel(private val repository: IRepository) : ViewModel() {
    fun getPrediction() = repository.getPrediction().asLiveData()
}