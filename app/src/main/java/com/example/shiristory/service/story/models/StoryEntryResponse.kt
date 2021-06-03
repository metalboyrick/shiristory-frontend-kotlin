package com.example.shiristory.service.story.models

import com.google.gson.annotations.SerializedName

data class StoryEntryResponse(
        @SerializedName("page")
        val page: Int = 1,
        @SerializedName("page_size")
        val pageSize: Int = 10,
        @SerializedName("total_pages")
        val totalPages: Int = 1,
        @SerializedName("stories")
        val stories:ArrayList<StoryEntry> = ArrayList<StoryEntry>(),
        @SerializedName("next")
        val next: String? = null,
        @SerializedName("previous")
        val previous: String? = null
)
