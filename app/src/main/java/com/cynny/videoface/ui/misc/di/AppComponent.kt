package com.cynny.videoface.ui.misc.di

import com.cynny.videoface.data.di.DataModule
import com.cynny.videoface.ui.VideoFaceApp
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AndroidInjectorBindingModule::class,
    AppModule::class,
    DataModule::class
])
interface AppComponent : AndroidInjector<VideoFaceApp> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<VideoFaceApp>()
}