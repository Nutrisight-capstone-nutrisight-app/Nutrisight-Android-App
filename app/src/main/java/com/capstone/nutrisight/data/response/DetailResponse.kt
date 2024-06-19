package com.capstone.nutrisight.data.response

import com.google.gson.annotations.SerializedName

data class DetailResponse(

	@field:SerializedName("product")
	val product: DetailProduct
)

data class DetailProduct(

	@field:SerializedName("sugarGrade")
	val sugarGrade: String,

	@field:SerializedName("energyTotal")
	val energyTotal: Int,

	@field:SerializedName("natriumLevel")
	val natriumLevel: Int,

	@field:SerializedName("levelAll")
	val levelAll: Int,

	@field:SerializedName("carbohydrate")
	val carbohydrate: Int,

	@field:SerializedName("fatLevel")
	val fatLevel: Int,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("sugarLevel")
	val sugarLevel: Int,

	@field:SerializedName("fatTotal")
	val fatTotal: Any,

	@field:SerializedName("saturatedFat")
	val saturatedFat: Int,

	@field:SerializedName("netWeight")
	val netWeight: Int,

	@field:SerializedName("servingAmount")
	val servingAmount: Int,

	@field:SerializedName("energyFat")
	val energyFat: Int,

	@field:SerializedName("protein")
	val protein: Int,

	@field:SerializedName("natriumGrade")
	val natriumGrade: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("fatGrade")
	val fatGrade: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("natrium")
	val natrium: Int,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("servingSize")
	val servingSize: Int,

	@field:SerializedName("sugar")
	val sugar: Int,

	@field:SerializedName("gradeAll")
	val gradeAll: String
)
