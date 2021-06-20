package com.example.shiristory.service.story.models

import com.google.gson.annotations.SerializedName

data class OutMessage(
    @SerializedName("biztype")
    var biztype: String,
    @SerializedName("group_id")
    var groupId: String ,
    @SerializedName("author")
    var author: String ,
    @SerializedName("story_type")
    val type: Int ,
    @SerializedName("story_content")
    val content: String,
    @SerializedName("next_story_type")
    val nextType: Int ,
    @SerializedName("vote_count")
    val voteCount: Int
)
