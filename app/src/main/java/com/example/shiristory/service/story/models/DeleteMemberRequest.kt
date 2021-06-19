package com.example.shiristory.service.story.models

import com.google.gson.annotations.SerializedName

data class DeleteMemberRequest(
    @SerializedName("member_id")
    val memberId: String
)
