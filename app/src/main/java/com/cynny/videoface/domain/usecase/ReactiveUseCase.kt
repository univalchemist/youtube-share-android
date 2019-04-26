package com.cynny.videoface.domain.usecase

import com.cynny.videoface.domain.misc.Resource
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


abstract class ReactiveUseCase<in P, R> {
    private val subject: PublishSubject<Resource<R>> = PublishSubject.create()
    private var disposable = CompositeDisposable()
    private val observable = subject.doOnDispose { disposable.clear() }

    fun observable(): Observable<Resource<R>> {
        return observable
    }

    fun execute(parameters: P) {
        disposable.clear()

        disposable.add(action(parameters)
                .subscribeOn(Schedulers.io())
                .subscribe { subject.onNext(it) })
    }

    protected abstract fun action(parameters: P): Observable<Resource<R>>
}

fun <R> ReactiveUseCase<Unit, R>.execute() = this.execute(Unit)