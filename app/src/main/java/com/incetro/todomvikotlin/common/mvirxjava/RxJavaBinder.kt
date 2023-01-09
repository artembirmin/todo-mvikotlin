/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.common.mvirxjava

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.Binder
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.binder.attachTo
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.assertOnMainThread
import com.arkivanov.mvikotlin.core.view.ViewRenderer
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.kotlin.subscribeBy

/**
 * A builder function for the [Binder]
 *
 * @param builder the DSL block function
 *
 * @return a new instance of the [Binder]
 */
fun bind(builder: RxJavaBindingsBuilder.() -> Unit): Binder =
    RxJavaBuilderBinder()
        .also(builder)

/**
 * A builder function for the [Binder]. Also attaches the created [Binder] to the provided [Lifecycle].
 * See [Binder.attachTo(...)][com.arkivanov.mvikotlin.core.binder.attachTo] for more information.
 *
 * @param lifecycle a [Lifecycle] to attach the created [Binder] to
 * @param mode a [BinderLifecycleMode] to be used when attaching the created [Binder] to the [Lifecycle]
 * @param builder the DSL block function
 *
 * @return a new instance of the [Binder]
 */
fun bind(
    lifecycle: Lifecycle,
    mode: BinderLifecycleMode,
    builder: RxJavaBindingsBuilder.() -> Unit
): Binder =
    bind(builder)
        .attachTo(lifecycle, mode)

interface RxJavaBindingsBuilder {

    /**
     * Creates a binding between this [Observable] and the provided `consumer`
     *
     * @receiver a stream of values
     * @param consumer a `consumer` of values
     */
    infix fun <T : Any> Observable<T>.bindTo(consumer: (T) -> Unit)

    /**
     * Creates a binding between this [Observable] and the provided `consumer`
     *
     * @receiver a stream of values
     * @param consumer a `consumer` of values represented as [Consumer]
     */
    infix fun <T : Any> Observable<T>.bindTo(consumer: Consumer<T>)

    /**
     * Creates a binding between this [Observable] and the provided [ViewRenderer]
     *
     * @receiver a stream of the `View Models`
     * @param viewRenderer a [ViewRenderer] that will consume the `View Models`
     */
    infix fun <Model : Any> Observable<Model>.bindTo(viewRenderer: ViewRenderer<Model>)

    /**
     * Creates a binding between this [Observable] and the provided [Store]
     *
     * @receiver a stream of the [Store] `States`
     * @param store a [Store] that will consume the `Intents`
     */
    infix fun <Intent : Any> Observable<Intent>.bindTo(store: Store<Intent, *, *>)
}

class RxJavaBuilderBinder : RxJavaBindingsBuilder, Binder {
    private val bindings = ArrayList<RxJavaBinding<*>>()
    private val compositeDisposable = CompositeDisposable()

    override fun <T : Any> Observable<T>.bindTo(consumer: (T) -> Unit) {
        bindings += RxJavaBinding(this, consumer)
    }

    override fun <T : Any> Observable<T>.bindTo(consumer: Consumer<T>) {
        this bindTo consumer::accept
    }

    override fun <Model : Any> Observable<Model>.bindTo(viewRenderer: ViewRenderer<Model>) {
        this bindTo {
            assertOnMainThread()
            viewRenderer.render(it)
        }
    }

    override fun <T : Any> Observable<T>.bindTo(store: Store<T, *, *>) {
        this bindTo store::accept
    }

    override fun start() {
        bindings.forEach { start(it) }
    }

    private fun <T : Any> start(binding: RxJavaBinding<T>) {
        binding
            .source
            .subscribeBy(onNext = binding.consumer)
            .addDisposable()
    }

    override fun stop() {
        compositeDisposable.clear()
    }

    private fun Disposable.addDisposable(): Disposable {
        compositeDisposable.add(this)
        return this
    }
}

private class RxJavaBinding<T : Any>(
    val source: Observable<T>,
    val consumer: (T) -> Unit
)