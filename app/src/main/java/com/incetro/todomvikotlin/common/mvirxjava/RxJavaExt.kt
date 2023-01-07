/*
 * todomvikotlin
 *
 * Created by artembirmin on 7/1/2023.
 */

package com.incetro.todomvikotlin.common.mvirxjava

import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.rx.Observer
import com.arkivanov.mvikotlin.rx.observer
import io.reactivex.rxjava3.core.Observable

internal inline fun <T, R : Any> T.toObservable(
    crossinline subscribe: T.(Observer<R>) -> Disposable
): Observable<R> =
    Observable.create { emitter ->
        val disposable = subscribe(
            observer(
                onComplete = emitter::onComplete,
                onNext = emitter::onNext
            )
        )
        emitter.setDisposable(disposable.toDisposable())
    }

internal fun Disposable.toDisposable(): io.reactivex.rxjava3.disposables.Disposable =
    object : io.reactivex.rxjava3.disposables.Disposable {

        override fun dispose() {
            this@toDisposable.dispose()
        }

        override fun isDisposed(): Boolean {
            return this@toDisposable.isDisposed
        }
    }