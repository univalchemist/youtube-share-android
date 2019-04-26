package com.cynny.videoface.data.source.remote.youtube.model

data class Item(
        val kind: String,
        val etag: String,
        val id: String,
        val snippet: Snippet,
        val contentDetails: ContentDetails,
        val statistics: Statistics
)