package com.cynny.videoface.ui.videoList

import com.cynny.videoface.domain.model.VideoDetail
import com.cynny.videoface.domain.model.Video


data class VideoListViewState(val videos: Map<Video, VideoDetail?> = LinkedHashMap(), val loading : Boolean = false)