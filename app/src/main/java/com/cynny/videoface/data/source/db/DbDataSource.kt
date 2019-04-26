package com.cynny.videoface.data.source.db

import com.cynny.videoface.data.source.db.model.VideoDbEntity
import com.cynny.videoface.data.source.db.model.VideoDetailDbEntity
import com.cynny.videoface.domain.misc.Resource
import com.cynny.videoface.domain.model.Video
import com.cynny.videoface.domain.model.VideoDetail
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber


class DbDataSource(private val database: AppDatabase) {
    fun getAllVideos(): Observable<Resource<List<Video>>> = database.videoDbEntityDao().getAll().map {
        Resource.Success(it.map { video -> VideoDbEntity.toDomain(video) })
    }

    fun getVideoDetail(id: String): Observable<Resource<VideoDetail?>> = database.videoDetailDbEntityDao().get(id).map {
        Timber.d("getVideoDetail id=%s = %s", id, it)
        if (it.isEmpty()) {
            Resource.Success(null)
        } else {
            Resource.Success(VideoDetailDbEntity.toDomain(it[0]))
        }
    }

    fun addVideoDetail(detail: VideoDetail): Completable = Completable.fromCallable {
        database.videoDetailDbEntityDao().insert(VideoDetailDbEntity.fromDomain(detail))
    }

    fun addVideo(video: Video): Single<Resource<Video>> = Single.fromCallable {
        database.videoDbEntityDao().insert(VideoDbEntity.fromDomain(video))
        Resource.Success(video)
    }

    fun getVideo(id: String): Observable<Resource<Video?>> = database.videoDbEntityDao().get(id).map {
        if (it.isEmpty()) {
            Resource.Success(null)
        } else {
            Resource.Success(VideoDbEntity.toDomain(it[0]))
        }
    }

    fun delete(video: Video): Single<Resource<Boolean>> = Single.fromCallable {
        database.videoDbEntityDao().remove(VideoDbEntity.fromDomain(video))
        Resource.Success(true)
    }
}