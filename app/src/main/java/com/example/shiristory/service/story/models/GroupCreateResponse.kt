package com.example.shiristory.service.story.models

import com.google.gson.annotations.SerializedName

data class GroupCreateResponse(
    @SerializedName("group_id")
    val group_id: String
)