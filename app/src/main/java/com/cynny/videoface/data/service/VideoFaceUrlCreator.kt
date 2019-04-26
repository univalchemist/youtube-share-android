package com.cynny.videoface.data.service

import com.cynny.videoface.domain.service.RemoteUrlCreator


class VideoFaceUrlCreator : RemoteUrlCreator {
    override fun getUrl(videoId: String) = "https://emotionaltrackingsdk.morphcast.com/video-face.html?id=$videoId"
}