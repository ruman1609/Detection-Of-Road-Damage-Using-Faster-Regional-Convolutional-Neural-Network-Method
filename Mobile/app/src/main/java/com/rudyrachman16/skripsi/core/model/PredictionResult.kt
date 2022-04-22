package com.rudyrachman16.skripsi.core.model

data class PredictionResult(
    val id: Int,
    val image_path: String,
    val objects: List<String>
)
