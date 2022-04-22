package com.rudyrachman16.skripsi.core.network.model

import com.google.gson.annotations.SerializedName

data class PredictionResultRes(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("objects")
	val objects: String? = null
)
