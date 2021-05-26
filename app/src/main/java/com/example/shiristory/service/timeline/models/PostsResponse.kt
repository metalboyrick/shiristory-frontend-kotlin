package com.example.shiristory.service.timeline.models

import com.google.gson.annotations.SerializedName

data class PostsResponse (
    @SerializedName("page")
    val page: Int = 1,
    @SerializedName("page_size")
    val pageSize: Int = 10,
    @SerializedName("total_pages")
    val totalPages: Int = 1,
    @SerializedName("posts")
    val posts:ArrayList<Post> = ArrayList<Post>(),
    @SerializedName("next")
    val next: String? = null,
    @SerializedName("previous")
    val previous: String? = null
)
