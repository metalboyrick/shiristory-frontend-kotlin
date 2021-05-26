package com.example.shiristory.service.timeline.models

import com.google.gson.annotations.SerializedName

data class SimpleUser(
    @SerializedName("_id")
    val id:String,
    @SerializedName("username")
    val username:String,
    @SerializedName("nickname")
    val nickname:String? = null,
    @SerializedName("profile_pic_url")
    val profilePicUrl:String
)