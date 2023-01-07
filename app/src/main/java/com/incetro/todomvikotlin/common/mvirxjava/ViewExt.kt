/*
 * todomvikotlin
 *
 * Created by artembirmin on 7/1/2023.
 */

package com.incetro.todomvikotlin.common.mvirxjava

import com.arkivanov.mvikotlin.core.annotations.MainThread
import com.arkivanov.mvikotlin.core.view.ViewEvents
import io.reactivex.rxjava3.core.Observable

/**
 * Returns a [Observable] that emits `View Events`
 * Emissions are performed on the main thread.
 */
@MainThread
val <Event : Any> ViewEvents<Event>.events: Observable<Event>
    get() = this.toObservable(ViewEvents<Event>::events)
