package com.cynny.videoface.data.repository

import com.cynny.videoface.data.source.remote.videoface.VideoFaceDataSource
import com.cynny.videoface.domain.misc.Resource
import com.cynny.videoface.domain.model.VideoStats
import com.cynny.videoface.domain.repository.StatsRepository
import io.reactivex.Observable


class StatsRepository(private val videoFaceDataSource: VideoFaceDataSource) : StatsRepository {
    override fun get(id: String): Observable<Resource<VideoStats>> = videoFaceDataSource.getStats(id).toObservable()
}