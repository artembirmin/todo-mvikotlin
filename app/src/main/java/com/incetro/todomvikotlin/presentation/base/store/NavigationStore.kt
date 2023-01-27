/*
 * todomvikotlin
 *
 * Created by artembirmin on 27/1/2023.
 */

package com.incetro.todomvikotlin.presentation.base.store

import com.arkivanov.mvikotlin.core.store.Store
import com.incetro.todomvikotlin.common.di.scope.FeatureScope
import com.incetro.todomvikotlin.common.mvibase.NavigationLabel
import com.incetro.todomvikotlin.common.mvirxjava.RxJavaExecutor
import com.incetro.todomvikotlin.presentation.base.store.NavigationStore.NavigationIntent
import javax.inject.Inject

abstract class NavigationStore :
    Store<NavigationIntent, Unit, NavigationLabel> {

    sealed class NavigationIntent {
        object OnBackPressed : NavigationIntent()
    }

    companion object {
        val NAME = this::class.simpleName!!
    }
}

@FeatureScope
class CommonNavigationStoreExecutor @Inject constructor() :
    RxJavaExecutor<NavigationIntent, Unit, Unit, Unit, NavigationLabel>() {

    override fun executeIntent(intent: NavigationIntent, getState: () -> Unit) {
        when (intent) {
            NavigationIntent.OnBackPressed -> publish(NavigationLabel.Exit)
        }
    }
}