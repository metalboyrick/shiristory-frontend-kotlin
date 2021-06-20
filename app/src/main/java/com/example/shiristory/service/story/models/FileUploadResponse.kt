package com.example.shiristory.service.story.models

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @SerializedName("file_url")
    val fileUrl : String
)
