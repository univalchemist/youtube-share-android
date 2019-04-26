package com.cynny.videoface.data.source.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cynny.videoface.domain.model.Thumbnail
import com.cynny.videoface.domain.model.VideoDetail


@Entity(tableName = "videodetail")
data class VideoDetailDbEntity(
        @PrimaryKey var id: String, var title: String, var channel: String, var duration: Long, var publishedAt: Long, var kind: String,
        var thumbnails: List<ThumbnailDbEntity>
) {
    companion object {
        fun toDomain(obj: VideoDetailDbEntity) = VideoDetail(obj.id, obj.title, obj.channel, obj.duration, obj.publishedAt, obj.kind,
                obj.thumbnails.map { Thumbnail(it.url, it.width, it.height) })

        fun fromDomain(obj: VideoDetail) = VideoDetailDbEntity(obj.id, obj.title, obj.channel, obj.duration, obj.publishedAt,
                obj.kind, ArrayList(obj.thumbnails.map { ThumbnailDbEntity(it.url, it.width, it.height) }))
    }
}

data class ThumbnailDbEntity(val url: String, val width: Int, val height: Int)