package com.cynny.videoface.data.repository

import com.cynny.videoface.data.source.db.DbDataSource
import com.cynny.videoface.data.source.remote.youtube.YoutubeDataSource
import com.cynny.videoface.domain.misc.Resource
import com.cynny.videoface.domain.misc.succeeded
import com.cynny.videoface.domain.model.VideoDetail
import com.cynny.videoface.domain.model.Video
import com.cynny.videoface.domain.repository.VideoRepository
import io.reactivex.Observable
import timber.log.Timber


class VideoRepository(private val dbDataSource: DbDataSource, private val youtubeDataSource: YoutubeDataSource) : VideoRepository {
    override fun delete(video: Video): Observable<Resource<Boolean>> {
        return dbDataSource.delete(video).toObservable()
    }

    override fun get(id: String): Observable<Resource<Video?>> = dbDataSource.getVideo(id)

    override fun add(video: Video): Observable<Resource<Video>> = dbDataSource.addVideo(video).toObservable()

    override fun getDetail(id: String): Observable<Resource<VideoDetail?>> {
        return dbDataSource.getVideoDetail(id)
                .flatMap { local ->
                    Timber.d("getDetail db id=%s = %s", id, local)
                    if (local.succeeded) {
                        Observable.just(local)
                    } else {
                        youtubeDataSource.getVideoDetail(id).toObservable()
                                .flatMap {
                                    Timber.d("getDetail remote id=%s = %s", id, it)
                                    if (it.succeeded) {
                                        dbDataSource.addVideoDetail((it as Resource.Success).data!!)
                                                .andThen(Observable.empty<Resource<VideoDetail>>())
                                    } else {
                                        Observable.just(it)
                                    }
                                }
                    }
                }
    }

    override fun getAll() = dbDataSource.getAllVideos()
}