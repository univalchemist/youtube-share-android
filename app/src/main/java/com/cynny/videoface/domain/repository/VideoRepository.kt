package com.cynny.videoface.domain.repository

import com.cynny.videoface.domain.misc.Resource
import com.cynny.videoface.domain.model.Video
import com.cynny.videoface.domain.model.VideoDetail
import io.reactivex.Observable

interface VideoRepository{
    fun getAll(): Observable<Resource<List<Video>>>
    fun getDetail(id: String): Observable<Resource<VideoDetail?>>
    fun add(video: Video): Observable<Resource<Video>>
    fun get(id: String): Observable<Resource<Video?>>
    fun delete(video: Video): Observable<Resource<Boolean>>
}
