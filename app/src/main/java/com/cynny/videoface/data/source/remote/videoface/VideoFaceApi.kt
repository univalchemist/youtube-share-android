package com.cynny.videoface.data.source.remote.videoface

import com.cynny.videoface.data.source.remote.videoface.model.VideoFaceStatApiResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface VideoFaceApi {
    @GET("stats/{id}")
    fun getStats(@Path("id") id: String): Single<List<VideoFaceStatApiResponse>>
}