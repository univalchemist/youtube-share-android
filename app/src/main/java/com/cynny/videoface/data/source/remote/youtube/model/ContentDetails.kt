package com.cynny.videoface.data.source.remote.youtube.model

data class ContentDetails(
        val duration: String,
        val dimension: String,
        val definition: String,
        val caption: String,
        val licensedContent: Boolean,
        val projection: String
)