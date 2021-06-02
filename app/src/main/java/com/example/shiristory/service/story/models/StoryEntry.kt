package com.example.shiristory.service.story.models

import com.google.gson.annotations.SerializedName

data class StoryEntry(
        @SerializedName("story_id")
        val storyId: String ,
        @SerializedName("author")
        val author: String ,
        @SerializedName("story_type")
        val type: Int ,
        @SerializedName("story_content")
        val content: String,
        @SerializedName("next_story_type")
        val nextType: Int ,
        @SerializedName("datetime")
        val datetime: String,
        @SerializedName("vote_count")
        val voteCount: Int
)
