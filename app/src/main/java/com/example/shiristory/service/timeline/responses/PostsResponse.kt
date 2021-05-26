package com.example.shiristory.service.timeline.responses

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

data class Post(
    @SerializedName("_id")
    val id:String,
    @SerializedName("author")
    val author:SimpleUser,
    @SerializedName("content")
    val content:String,
    @SerializedName("inv_link")
    val invLink:String? = null,
    @SerializedName("media")
    val mediaUrl:String? = null,
    @SerializedName("media_type")
    val mediaType:String? = null,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt:String,
    @SerializedName("likes")
    var likes:ArrayList<SimpleUser> = ArrayList<SimpleUser>(),
    @SerializedName("comments")
    var comments:ArrayList<Comment> = ArrayList<Comment>()
)

data class Comment(
    @SerializedName("author")
    val author:SimpleUser,
    @SerializedName("comment")
    val comment:String,
    @SerializedName("created_at")
    val createdAt:String
)

data class SimpleUser(
    @SerializedName("_id")
    val id:String,
    @SerializedName("username")
    val username:String,
    @SerializedName("nickname")
    val nickname:String? = null,
    @SerializedName("profile_pic_url")
    val profilePicUrl:String
)