package com.rudyrachman16.skripsi.core

import com.rudyrachman16.skripsi.core.model.PredictionResult
import com.rudyrachman16.skripsi.core.network.model.PredictionResultRes

object MapVal {
    fun predFromResToMod(result: PredictionResultRes): PredictionResult {
        result.apply {
            return PredictionResult(
                id ?: -1, image ?: "",
                objects?.split("\n")?.filter { it.isNotBlank() } ?: listOf())
        }
    }
}