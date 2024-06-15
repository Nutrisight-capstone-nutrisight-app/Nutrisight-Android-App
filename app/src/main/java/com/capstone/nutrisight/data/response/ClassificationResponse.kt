package com.capstone.nutrisight.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ClassificationResponse(

	@field:SerializedName("product")
	val product: Product
) : Parcelable

@Parcelize
data class Product(

	@field:SerializedName("sugarGrade")
	val sugarGrade: String?,

	@field:SerializedName("energyTotal")
	val energyTotal: Int?,

	@field:SerializedName("natriumLevel")
	val natriumLevel: Int?,

	@field:SerializedName("updateAt")
	val updateAt: String?,

	@field:SerializedName("levelAll")
	val levelAll: Int?,

	@field:SerializedName("carbohydrate")
	val carbohydrate: Int?,

	@field:SerializedName("fatLevel")
	val fatLevel: Int?,

	@field:SerializedName("sugarLevel")
	val sugarLevel: Int?,

	@field:SerializedName("createdAt")
	val createdAt: String?,

	@field:SerializedName("fatTotal")
	val fatTotal: @RawValue Any?,

	@field:SerializedName("saturatedFat")
	val saturatedFat: Int?,

	@field:SerializedName("netWeight")
	val netWeight: Int?,

	@field:SerializedName("energyFat")
	val energyFat: Int?,

	@field:SerializedName("servingAmount")
	val servingAmount: Int?,

	@field:SerializedName("natriumGrade")
	val natriumGrade: String?,

	@field:SerializedName("protein")
	val protein: Int?,

	@field:SerializedName("name")
	val name: String?,

	@field:SerializedName("fatGrade")
	val fatGrade: String?,

	@field:SerializedName("id")
	val id: Int?,

	@field:SerializedName("natrium")
	val natrium: Int?,

	@field:SerializedName("category")
	val category: String?,

	@field:SerializedName("servingSize")
	val servingSize: Int?,

	@field:SerializedName("sugar")
	val sugar: Int?,

	@field:SerializedName("gradeAll")
	val gradeAll: String?,

	@field:SerializedName("url")
	val url: String?
) : Parcelable
