package com.example.shiristory.service.story.models

import com.google.gson.annotations.SerializedName

data class GroupListResponse(
        @SerializedName("page")
        val page: Int = 1,
        @SerializedName("page_size")
        val pageSize: Int = 10,
        @SerializedName("total_pages")
        val totalPages: Int = 1,
        @SerializedName("groups")
        val groups:ArrayList<GroupListEntry> = ArrayList<GroupListEntry>(),
        @SerializedName("next")
        val next: String? = null,
        @SerializedName("previous")
        val previous: String? = null
)
