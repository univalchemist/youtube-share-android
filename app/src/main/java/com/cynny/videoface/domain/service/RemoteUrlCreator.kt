package com.cynny.videoface.domain.service


interface RemoteUrlCreator {
    fun getUrl(videoId: String): String
}