package com.example.shiristory.service.common

import com.example.shiristory.service.timeline.responses.SimpleUser
import com.google.gson.annotations.SerializedName

data class GenericResponse(
    @SerializedName("message")
    val message: String? = null
)