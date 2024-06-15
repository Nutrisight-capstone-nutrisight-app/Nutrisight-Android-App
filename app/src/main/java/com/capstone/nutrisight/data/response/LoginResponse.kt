package com.capstone.nutrisight.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("accessToken")
	val accessToken: String,

	@field:SerializedName("error")
	val error: String,
	
)
