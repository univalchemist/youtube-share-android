package com.cynny.videoface.data.source.remote.youtube.model

data class Snippet(
        val publishedAt: String,
        val channelId: String,
        val title: String,
        val description: String,
        val thumbnails: Thumbnails,
        val channelTitle: String,
        val tags: List<String>,
        val categoryId: String,
        val liveBroadcastContent: String,
        val defaultLanguage: String,
        val localized: Localized,
        val defaultAudioLanguage: String
)