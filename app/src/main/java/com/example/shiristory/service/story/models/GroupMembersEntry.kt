package com.example.shiristory.service.story.models

import com.google.gson.annotations.SerializedName

data class GroupMembersEntry(
    @SerializedName("_id")
    val id:String,
    @SerializedName("username")
    val username: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("profile_pic_url")
    val profilePicUrl: String,
    @SerializedName("nickname")
    val nickname: String
)
