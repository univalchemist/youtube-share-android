package com.cynny.videoface.ui.misc.di

import com.cynny.videoface.ui.main.MainActivity
import com.cynny.videoface.ui.videoDetail.VideoDetailFragment
import com.cynny.videoface.ui.videoList.VideoListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class AndroidInjectorBindingModule {
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindVideoListFragment(): VideoListFragment

    @ContributesAndroidInjector
    abstract fun bindVideoDetailFragment(): VideoDetailFragment
}