package com.example.shiristory.service.common

enum class MediaType(val value: String, val id: Int) {
    TEXT("text/*", 0),
    IMAGE("image/*", 1),
    VIDEO("video/*", 2),
    AUDIO("audio/*", 3)
}