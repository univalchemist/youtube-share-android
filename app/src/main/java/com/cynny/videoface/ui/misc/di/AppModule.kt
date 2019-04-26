package com.cynny.videoface.ui.misc.di

import android.app.Application
import com.cynny.videoface.ui.VideoFaceApp
import com.cynny.videoface.ui.misc.service.ShareService
import com.cynny.videoface.ui.misc.utils.ViewModelFactory
import dagger.Module
import dagger.Binds
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AppModule {
    @Binds
    abstract fun application(app: VideoFaceApp): Application

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideViewModelFactory(): ViewModelFactory = ViewModelFactory()

        @JvmStatic
        @Provides
        @Singleton
        fun provideShareService(): ShareService = ShareService()
    }
}