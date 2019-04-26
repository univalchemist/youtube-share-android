package com.cynny.videoface.data.source.db

import androidx.room.TypeConverter
import com.cynny.videoface.data.source.db.model.ThumbnailDbEntity
import com.google.gson.Gson


class Converters {
    @TypeConverter
    fun listToJson(value: List<ThumbnailDbEntity>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<ThumbnailDbEntity>? {
        val objects = Gson().fromJson(value, Array<ThumbnailDbEntity>::class.java) as Array<ThumbnailDbEntity>
        return objects.toList()
    }
}