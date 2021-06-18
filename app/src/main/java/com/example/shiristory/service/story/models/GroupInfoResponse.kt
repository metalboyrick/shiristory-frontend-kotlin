package com.example.shiristory.service.story.models

import com.google.gson.annotations.SerializedName

data class GroupInfoResponse (
    @SerializedName("group_name")
    val name : String,
    @SerializedName("group_status")
    val status: Int,
    @SerializedName("group_members")
    val members: ArrayList<GroupMembersEntry> = ArrayList<GroupMembersEntry>(),
    @SerializedName("group_admins")
    val admins: ArrayList<GroupMembersEntry> = ArrayList<GroupMembersEntry>()
)