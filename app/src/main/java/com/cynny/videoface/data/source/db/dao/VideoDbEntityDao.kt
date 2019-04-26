package com.cynny.videoface.data.source.db.dao

import androidx.room.*
import com.cynny.videoface.data.source.db.model.VideoDbEntity
import io.reactivex.Observable


@Dao
interface VideoDbEntityDao {
    @Query("SELECT * from video")
    fun getAll(): Observable<List<VideoDbEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(video: VideoDbEntity)

    @Query("SELECT * from video where id = :id")
    fun get(id: String): Observable<List<VideoDbEntity>>

    @Delete
    fun remove(video: VideoDbEntity)
}