package com.example.shiristory.service.timeline.models

import com.example.shiristory.service.authentication.models.User
import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("author")
    val author: User,
    @SerializedName("comment")
    val comment:String,
    @SerializedName("created_at")
    val createdAt:String
)