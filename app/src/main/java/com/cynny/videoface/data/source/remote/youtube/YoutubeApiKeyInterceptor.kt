package com.cynny.videoface.data.source.remote.youtube

import okhttp3.Interceptor
import okhttp3.Response


class YoutubeApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
                .addQueryParameter("key", "AIzaSyAyEnjmPx3Ix2uDCWtFK_cxvUkOR2YyBb8")
                .build()

        val requestBuilder = original.newBuilder()
                .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}