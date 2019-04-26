package com.cynny.videoface.data.source.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cynny.videoface.domain.model.Video

@Entity(tableName = "video")
data class VideoDbEntity(
        @PrimaryKey var id: String,
        var srcUrl: String,
        var remoteUrl: String,
        var addedAt: Long
) {
    companion object {
        fun toDomain(obj: VideoDbEntity) = Video(obj.id, obj.srcUrl, obj.remoteUrl, obj.addedAt)
        fun fromDomain(obj: Video) = VideoDbEntity(obj.id, obj.srcUrl, obj.remoteUrl, obj.addedAt)
    }
}


