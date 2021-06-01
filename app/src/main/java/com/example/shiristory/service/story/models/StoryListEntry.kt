package com.example.shiristory.service.story.models

import com.google.gson.annotations.SerializedName

data class StoryListEntry (
    @SerializedName("group_id")
    val groupId: String,
    @SerializedName("group_name")
    val name : String,
    @SerializedName("group_summary")
    val summary: String,
    @SerializedName("group_last_edited")
    val lastEdited: String
)