package com.example.shiristory.service.timeline.models

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("author")
    val author:SimpleUser,
    @SerializedName("comment")
    val comment:String,
    @SerializedName("created_at")
    val createdAt:String
)