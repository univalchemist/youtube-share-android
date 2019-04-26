package com.cynny.videoface.data.source.remote.youtube.model

import com.cynny.videoface.domain.model.Thumbnail
import com.cynny.videoface.domain.model.VideoDetail
import java.text.SimpleDateFormat
import java.util.*

data class YoutubeListApiResponse(
        val kind: String,
        val etag: String,
        val pageInfo: PageInfo,
        val items: List<Item>
) {
    companion object {
        fun toDomain(obj: YoutubeListApiResponse): VideoDetail? {
            return obj.items.getOrNull(0)?.let {
                val tbs = it.snippet.thumbnails
                val thumbnails = mutableListOf<Thumbnail>()
                val addThumbnail = { t: com.cynny.videoface.data.source.remote.youtube.model.Thumbnail ->
                    thumbnails.add(Thumbnail(t.url, t.width, t.height))
                }
                tbs.default?.let {
                    addThumbnail(it)
                }
                tbs.medium?.let {
                    addThumbnail(it)
                }
                tbs.high?.let {
                    addThumbnail(it)
                }
                tbs.standard?.let {
                    addThumbnail(it)
                }
                tbs.maxres?.let {
                    addThumbnail(it)
                }

                VideoDetail(it.id, it.snippet.title, it.snippet.channelTitle,
                        duration(it.contentDetails.duration),
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(it.snippet.publishedAt).time,
                        it.kind,
                        thumbnails)
            }
        }

        private fun duration(src: String): Long {
            var time = src.substring(2)
            var duration = 0L
            val seps = listOf("H", "M", "S")
            val secs = listOf(3600, 60, 1)
            seps.forEachIndexed { idx, sep ->
                val index = time.indexOf(sep)
                if (index != -1) {
                    val value = time.substring(0, index)
                    duration += Integer.parseInt(value) * secs[idx] * 1000
                    time = time.substring(value.length + 1)
                }
            }
            return duration
        }
    }
}