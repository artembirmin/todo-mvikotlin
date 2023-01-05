package com.incetro.todomvikotlin.common.mvirxjava

import com.arkivanov.mvikotlin.core.store.Bootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.atomic.AtomicReference

/**
 * An abstract implementation of the [Bootstrapper] that provides interoperability with RxJava.
 */
abstract class RxJavaBootstrapper<Action : Any> : Bootstrapper<Action> {

    private val actionConsumer = AtomicReference<(Action) -> Unit>()
    private val compositeDisposable = CompositeDisposable()

    final override fun init(actionConsumer: (Action) -> Unit) {
        this.actionConsumer.set(actionConsumer)
    }

    /**
     * Dispatches the `Action` to the [Store]
     *
     * @param action an `Action` to be dispatched
     */
    protected fun dispatch(action: Action) {
        actionConsumer.get().invoke(action)
    }

    override fun dispose() {
        compositeDisposable.dispose()
    }

    protected fun Disposable.addDisposable(): Disposable {
        compositeDisposable.add(this)
        return this
    }
}
