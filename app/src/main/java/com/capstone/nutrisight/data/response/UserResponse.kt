package com.capstone.nutrisight.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("user")
	val user: User
)

data class User(

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)

data class Data(

	@field:SerializedName("foodCal")
	val foodCal: Int,

	@field:SerializedName("totalCal")
	val totalCal: Int,

	@field:SerializedName("drinkCal")
	val drinkCal: Int,

	@field:SerializedName("gradeAvg")
	val gradeAvg: String,

	@field:SerializedName("saveAmount")
	val saveAmount: Int
)
