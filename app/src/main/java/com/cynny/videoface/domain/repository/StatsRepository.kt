package com.cynny.videoface.domain.repository

import com.cynny.videoface.domain.misc.Resource
import com.cynny.videoface.domain.model.VideoStats
import io.reactivex.Observable

interface StatsRepository{
    fun get(id: String): Observable<Resource<VideoStats>>
}
