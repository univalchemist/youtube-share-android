package com.cynny.videoface.data.source.remote.videoface

import com.cynny.videoface.data.source.remote.videoface.model.VideoFaceStatApiResponse
import com.cynny.videoface.domain.misc.Resource
import com.cynny.videoface.domain.model.VideoStats
import io.reactivex.Single


class VideoFaceDataSource(private val videoFaceApi: VideoFaceApi) {
    fun getStats(id: String): Single<Resource<VideoStats>> = videoFaceApi.getStats(id)
            .map { it -> Resource.Success(it.asSequence().map { VideoFaceStatApiResponse.toDomain(it) }.groupBy { it.startTime }.toSortedMap()) as Resource<VideoStats> }
            .onErrorReturn { Resource.Error(it) }
}