package com.example.shiristory.service.authentication.models

import com.example.shiristory.service.authentication.models.User
import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("author")
    val author: User,
    @SerializedName("comment")
    val comment:String,
    @SerializedName("created_at")
    val createdAt:String
)