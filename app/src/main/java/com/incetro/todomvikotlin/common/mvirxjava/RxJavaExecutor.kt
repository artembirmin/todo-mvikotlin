/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.common.mvirxjava

import com.arkivanov.mvikotlin.core.annotations.MainThread
import com.arkivanov.mvikotlin.core.store.Bootstrapper
import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.atomic.AtomicReference

abstract class RxJavaExecutor<in Intent : Any, in Action : Any, in State : Any, Message : Any, Label : Any> :
    Executor<Intent, Action, State, Message, Label> {

    private val callbacks = AtomicReference<Executor.Callbacks<State, Message, Label>>()
    private val compositeDisposable = CompositeDisposable()

    private val getState: () -> State = { callbacks.get().state }

    final override fun init(callbacks: Executor.Callbacks<State, Message, Label>) {
        this.callbacks.set(callbacks)
    }

    final override fun executeIntent(intent: Intent) {
        executeIntent(intent, getState)
    }

    final override fun executeAction(action: Action) {
        executeAction(action, getState)
    }

    /**
     * The companion of the [Executor.executeIntent] method
     *
     * @param intent an `Intent` received by the [Store]
     * @param getState a `State` supplier that returns the *current* `State` of the [Store]
     */
    @MainThread
    protected open fun executeIntent(intent: Intent, getState: () -> State) {
    }

    /**
     * The companion of the [Executor.executeAction] method
     *
     * @param action an `Action` produced by the [Bootstrapper]
     * @param getState a `State` supplier that returns the *current* `State` of the [Store]
     */
    @MainThread
    protected open fun executeAction(action: Action, getState: () -> State) {
    }

    final override fun dispose() {
        compositeDisposable.dispose()
    }

    /**
     * Dispatches the provided `Message` to the [Reducer].
     * The updated `State` will be available immediately after this method returns.
     *
     * @param message a `Message` to be dispatched to the `Reducer`
     */
    @MainThread
    protected fun dispatch(message: Message) {
        callbacks.get().onMessage(message)
    }

    /**
     * Sends the provided `Label` to the [Store] for publication
     *
     * @param label a `Label` to be published
     */
    @MainThread
    protected fun publish(label: Label) {
        callbacks.get().onLabel(label)
    }

    protected fun Disposable.addDisposable(): Disposable {
        compositeDisposable.add(this)
        return this
    }
}