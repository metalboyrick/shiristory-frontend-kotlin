package com.example.shiristory.service.user.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    val id:String,
    @SerializedName("username")
    val username:String,
    @SerializedName("email")
    val email:String,
    @SerializedName("profile_pic_url")
    val profile_pic_url:String? = null,
    @SerializedName("nickname")
    val nickname:String? = null,
    @SerializedName("bio")
    val bio:String? = null,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt:String,
    @SerializedName("friends")
    var friends:ArrayList<User> = ArrayList<User>()

)