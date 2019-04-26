package com.cynny.videoface.domain.model

data class VideoDetail(val id: String, val title: String, val channel: String, val duration: Long, val publishedAt: Long, val kind: String,
                       val thumbnails: List<Thumbnail>)

data class Thumbnail(val url: String, val width: Int, val height: Int)