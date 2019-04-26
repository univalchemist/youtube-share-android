package com.cynny.videoface.ui.videoList

import androidx.lifecycle.ViewModel
import com.cynny.videoface.domain.misc.Resource
import com.cynny.videoface.domain.misc.succeeded
import com.cynny.videoface.domain.model.Video
import com.cynny.videoface.domain.model.VideoDetail
import com.cynny.videoface.domain.usecase.*
import com.cynny.videoface.ui.misc.livedata.ErrorLiveEvent
import com.cynny.videoface.ui.misc.livedata.ViewStateStore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


class VideoListViewModel @Inject constructor(private val getVideosUseCase: GetVideosUseCase,
                                             private val getVideoDetailUseCase: GetVideoDetailUseCase,
                                             private val deleteVideoUseCase: DeleteVideoUseCase) : ViewModel(), VideoDetailRequestListener {
    val store: ViewStateStore<VideoListViewState> = ViewStateStore(VideoListViewState())
    val errorLiveEvent = ErrorLiveEvent()
    private val disposable = CompositeDisposable()

    init {
        loadVideos()
    }

    private fun loadVideos() {
        Timber.d("loadVideos")
        store.dispatchState(store.state().copy(loading = true))
        disposable.add(
                getVideosUseCase.observable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { videosResource ->
                            Timber.d("videosResource = %s", videosResource)
                            when (videosResource) {
                                is Resource.Success -> {
                                    val m: MutableMap<Video, VideoDetail?> = LinkedHashMap()
                                    videosResource.data.sortedByDescending { it.addedAt }.forEach {
                                        m[it] = store.state().videos[it]
                                    }
                                    store.dispatchState(store.state().copy(videos = m.toMap(), loading = false))
                                }
                                is Resource.Error -> {
                                    store.dispatchState(store.state().copy(loading = false))
                                    errorLiveEvent.value = videosResource.exception.message
                                }
                            }
                        })
        getVideosUseCase.execute()
    }

    override fun onDetailRequest(id: String) {
        Timber.d("onDetailRequest id=%s", id)
        disposable.add(
                getVideoDetailUseCase.observable(GetVideoDetailUseCase.GetVideoDetailUseCaseParameters(id))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { detailResource ->
                            Timber.d("detailResource = %s", detailResource)
                            when (detailResource) {
                                is Resource.Success -> {
                                    val m = store.state().videos.toMutableMap()
                                    m.forEach {
                                        if (it.key.id == detailResource.data?.id) {
                                            m[it.key] = detailResource.data
                                            return@forEach
                                        }
                                    }
                                    store.dispatchState(store.state().copy(videos = m.toMap(), loading = false))
                                }
                            }
                        }
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun onDelete(video: Video) {
        disposable.add(deleteVideoUseCase.observable(DeleteVideoUseCase.DeleteVideoUseCaseParameters(video))
                .subscribe()
        )
    }
}
