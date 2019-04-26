package com.cynny.videoface.data.source.db.dao

import androidx.room.*
import com.cynny.videoface.data.source.db.model.VideoDetailDbEntity
import io.reactivex.Observable


@Dao
interface VideoDetailDbEntityDao {
    @Query("SELECT * from videodetail where id = :id")
    fun get(id: String): Observable<List<VideoDetailDbEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(detail: VideoDetailDbEntity)
}