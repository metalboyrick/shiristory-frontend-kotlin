package com.example.shiristory.service.authentication.models

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("access")
    val access:String? = null,
    @SerializedName("refresh")
    val refresh:String? = null
)