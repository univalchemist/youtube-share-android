package com.cynny.videoface.domain.usecase

import com.cynny.videoface.domain.misc.Resource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class UseCase<in P, R> {
    fun observable(parameters: P) = action(parameters)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    protected abstract fun action(parameters: P): Observable<Resource<R>>
}

fun <R> UseCase<Unit, R>.observable() = this.observable(Unit)