package com.capstone.nutrisight.data.response

import com.google.gson.annotations.SerializedName

data class RegisterDeleteResponse(
	@SerializedName("message")
	val message: String?,

	@SerializedName("error")
	val error: String?
)
