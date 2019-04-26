package com.cynny.videoface.data.source.db

import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.TypeConverters
import com.cynny.videoface.data.source.db.dao.VideoDbEntityDao
import com.cynny.videoface.data.source.db.dao.VideoDetailDbEntityDao
import com.cynny.videoface.data.source.db.model.VideoDbEntity
import com.cynny.videoface.data.source.db.model.VideoDetailDbEntity


@Database(entities = [VideoDbEntity::class, VideoDetailDbEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoDbEntityDao(): VideoDbEntityDao
    abstract fun videoDetailDbEntityDao(): VideoDetailDbEntityDao
}