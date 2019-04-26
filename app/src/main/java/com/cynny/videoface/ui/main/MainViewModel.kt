package com.cynny.videoface.ui.main

import androidx.lifecycle.ViewModel
import com.cynny.videoface.domain.misc.succeeded
import com.cynny.videoface.domain.usecase.AddVideoUseCase
import com.cynny.videoface.ui.misc.livedata.ShareLiveEvent
import com.cynny.videoface.domain.misc.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject


class MainViewModel @Inject constructor(private val addVideoUseCase: AddVideoUseCase) : ViewModel() {
    val shareMessageLiveEvent = ShareLiveEvent()
    private val disposable = CompositeDisposable()

    fun onExternalShare(url: String) {
        disposable.add(addVideoUseCase.observable(AddVideoUseCase.AddVideoUseCaseParameters(url))
                .subscribeBy {
                    if (it.succeeded) {
                        shareMessageLiveEvent.postValue((it as Resource.Success).data.remoteUrl)
                    }
                })
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
