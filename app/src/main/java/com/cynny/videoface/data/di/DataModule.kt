package com.cynny.videoface.data.di

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import androidx.room.Room
import com.cynny.videoface.data.service.VideoFaceUrlCreator
import com.cynny.videoface.data.service.YoutubeIdExtractor
import com.cynny.videoface.data.source.db.AppDatabase
import com.cynny.videoface.data.source.db.DbDataSource
import com.cynny.videoface.data.source.remote.misc.createService
import com.cynny.videoface.data.source.remote.videoface.VideoFaceApi
import com.cynny.videoface.data.source.remote.videoface.VideoFaceDataSource
import com.cynny.videoface.data.source.remote.videoface.VideoFaceFakeInterceptor
import com.cynny.videoface.data.source.remote.youtube.YoutubeApi
import com.cynny.videoface.data.source.remote.youtube.YoutubeApiKeyInterceptor
import com.cynny.videoface.data.source.remote.youtube.YoutubeDataSource
import com.cynny.videoface.domain.service.IdExtractor
import com.cynny.videoface.domain.service.RemoteUrlCreator
import okhttp3.HttpUrl


@Module
class DataModule {
    @Provides
    @Singleton
    fun provideDatabase(application: Application) = Room.databaseBuilder(application, AppDatabase::class.java, "videoface-db").fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideYoutubeApi() = createService<YoutubeApi>(HttpUrl.parse("https://www.googleapis.com/youtube/v3/")!!, listOf(YoutubeApiKeyInterceptor()))

    @Provides
    @Singleton
    fun provideYoutubeDataSource(youtubeApi: YoutubeApi) = YoutubeDataSource(youtubeApi)

    @Provides
    @Singleton
    fun provideVideoFaceApi() = createService<VideoFaceApi>(HttpUrl.parse("https://apiface.morphcast.com/v1/")!!)

    @Provides
    @Singleton
    fun provideVideoFaceDataSource(videoFaceApi: VideoFaceApi) = VideoFaceDataSource(videoFaceApi)

    @Provides
    @Singleton
    fun provideDbDataSource(database: AppDatabase) = DbDataSource(database)

    @Provides
    @Singleton
    fun provideVideoRepository(dbDataSource: DbDataSource, remoteDataSource: YoutubeDataSource): com.cynny.videoface.domain.repository.VideoRepository = com.cynny.videoface.data.repository.VideoRepository(dbDataSource, remoteDataSource)

    @Provides
    @Singleton
    fun provideVideoStatsRepository(remoteDataSource: VideoFaceDataSource): com.cynny.videoface.domain.repository.StatsRepository = com.cynny.videoface.data.repository.StatsRepository(remoteDataSource)

    @Provides
    @Singleton
    fun provideIdExtractor(): IdExtractor = YoutubeIdExtractor()

    @Provides
    @Singleton
    fun provideVideoFaceUrlCreator(): RemoteUrlCreator = VideoFaceUrlCreator()
}