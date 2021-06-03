package com.example.shiristory.service.user.models

import com.google.gson.annotations.SerializedName

data class SearchFriendResponse (
    @SerializedName("candidates")
    var friends: ArrayList<User> = ArrayList<User>()
)