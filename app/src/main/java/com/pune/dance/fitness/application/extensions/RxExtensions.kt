package com.pune.dance.fitness.application.extensions

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.subscribeOnBackObserverOnMain(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.subscribeOnBackObserverOnMain(): Flowable<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.subscribeOnBackObserverOnMain(): Single<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun Completable.subscribeOnBackObserverOnMain(): Completable {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun Disposable.addTo(disposable: CompositeDisposable) {
    disposable.add(this)
}

fun <T> Single<T>.flatMapResumeAfter(block: (T) -> Completable): Single<T> {
    return this.flatMap { t ->
        block(t).andThen(Single.just(t))
    }
}

fun <T> Observable<T>.flatMapResumeAfter(block: (T) -> Completable): Observable<T> {
    return this.flatMap { t ->
        block(t).andThen(Observable.just(t))
    }
}