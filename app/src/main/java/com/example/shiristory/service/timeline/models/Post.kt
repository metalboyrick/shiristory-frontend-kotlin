package com.example.shiristory.service.timeline.models

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("_id")
    val id:String,
    @SerializedName("author")
    val author:SimpleUser,
    @SerializedName("content")
    val content:String,
    @SerializedName("inv_link")
    val invLink:String? = null,
    @SerializedName("media")
    val mediaUrl:String? = null,
    @SerializedName("media_type")
    val mediaType:String? = null,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt:String,
    @SerializedName("likes")
    var likes:ArrayList<SimpleUser> = ArrayList<SimpleUser>(),
    @SerializedName("comments")
    var comments:ArrayList<Comment> = ArrayList<Comment>()
)