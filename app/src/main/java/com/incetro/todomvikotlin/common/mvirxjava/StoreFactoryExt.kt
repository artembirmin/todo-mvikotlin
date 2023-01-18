/*
 * todomvikotlin
 *
 * Created by artembirmin on 17/1/2023.
 */

package com.incetro.todomvikotlin.common.mvirxjava

import com.arkivanov.mvikotlin.core.store.*

fun <Intent : Any, Message : Any, State : Any, Label : Any> StoreFactory.createStoreSimple(
    name: String? = null,
    initialState: State,
    executor: Executor<Intent, Unit, State, Message, Label>,
    reducer: Reducer<State, Message>,
    autoInit: Boolean = false,
    bootstrapper: Bootstrapper<Unit>? = SimpleBootstrapper(Unit)
): Store<Intent, State, Label> {
    return this.createStore(
        name = name,
        autoInit = autoInit,
        initialState = initialState,
        bootstrapper = bootstrapper,
        executor = executor,
        reducer = reducer
    )
}

fun <Intent : Any, Action : Any, Message : Any, State : Any, Label : Any> StoreFactory.createStore(
    name: String? = null,
    initialState: State,
    executor: Executor<Intent, Action, State, Message, Label>,
    reducer: Reducer<State, Message>,
    autoInit: Boolean = false,
    bootstrapper: Bootstrapper<Action>? = null
): Store<Intent, State, Label> {
    return this.create(
        name = name,
        autoInit = autoInit,
        initialState = initialState,
        bootstrapper = bootstrapper,
        executorFactory = { executor },
        reducer = reducer
    )
}
