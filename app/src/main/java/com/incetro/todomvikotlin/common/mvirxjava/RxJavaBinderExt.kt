/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.common.mvirxjava

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.view.ViewEvents
import com.arkivanov.mvikotlin.rx.Observer
import com.arkivanov.mvikotlin.rx.observer
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable

fun <Event : Any> ViewEvents<Event>.observableEvents(): Observable<Event> {
    return wrapRxObservable {
        events(it)
    }
}

fun <State : Any> Store<*, State, *>.observableStates(): Observable<State> {
    return wrapRxObservable {
        states(it)
    }
}

fun <Label : Any> Store<*, *, Label>.observableLabels(): Observable<Label> {
    return wrapRxObservable {
        labels(it)
    }
}

private inline fun <T : Any> wrapRxObservable(
    crossinline subscribe: (Observer<T>) -> com.arkivanov.mvikotlin.rx.Disposable
): Observable<T> {
    return Observable.create { emitter ->
        val disposable = subscribe(observer(
            onComplete = {
                emitter.onComplete()
            },
            onNext = {
                emitter.onNext(it)
            }
        ))
        emitter.setDisposable(disposable.toRxDisposable())
    }
}

private fun com.arkivanov.mvikotlin.rx.Disposable.toRxDisposable(): Disposable {
    return object : Disposable {
        override fun dispose() {
            this@toRxDisposable.dispose()
        }

        override fun isDisposed(): Boolean {
            return this@toRxDisposable.isDisposed
        }

    }
}