package com.cynny.videoface.data.source.remote.youtube

import com.cynny.videoface.data.source.remote.youtube.model.YoutubeListApiResponse
import com.cynny.videoface.domain.misc.Resource
import com.cynny.videoface.domain.model.VideoDetail
import io.reactivex.Single


class YoutubeDataSource(private val youtubeApi: YoutubeApi) {
    fun getVideoDetail(id: String): Single<Resource<VideoDetail?>> = youtubeApi.getVideos(id)
            .map { Resource.Success(YoutubeListApiResponse.toDomain(it)) as Resource<VideoDetail?> }
            .onErrorReturn { Resource.Error(it) }
}