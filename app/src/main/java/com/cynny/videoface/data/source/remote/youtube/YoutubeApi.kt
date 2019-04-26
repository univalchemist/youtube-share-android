package com.cynny.videoface.data.source.remote.youtube

import com.cynny.videoface.data.source.remote.youtube.model.YoutubeListApiResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface YoutubeApi {
    @GET("videos/?part=snippet%2CcontentDetails%2Cstatistics")
    fun getVideos(@Query("id") id: String): Single<YoutubeListApiResponse>
}