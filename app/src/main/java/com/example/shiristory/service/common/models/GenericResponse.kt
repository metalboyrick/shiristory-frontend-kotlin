package com.example.shiristory.service.common.models

import com.google.gson.annotations.SerializedName

data class GenericResponse(
    @SerializedName("message")
    val message: String? = null
)