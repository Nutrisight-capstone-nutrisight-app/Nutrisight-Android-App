package com.capstone.nutrisight.data.response

import com.google.gson.annotations.SerializedName

data class SaveResponse(

    @field:SerializedName("message")
    val message: String
)

data class SaveProductRequest(
    val productId: Int?
)
