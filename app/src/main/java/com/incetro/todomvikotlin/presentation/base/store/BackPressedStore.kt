/*
 * todomvikotlin
 *
 * Created by artembirmin on 27/1/2023.
 */

package com.incetro.todomvikotlin.presentation.base.store

import com.arkivanov.mvikotlin.core.store.Store
import com.incetro.todomvikotlin.common.di.scope.FeatureScope
import com.incetro.todomvikotlin.common.mvicommon.NavigationLabel
import com.incetro.todomvikotlin.common.mvirxjava.RxJavaExecutor
import com.incetro.todomvikotlin.presentation.base.store.BackPressedStore.BackPressedIntent
import javax.inject.Inject

abstract class BackPressedStore :
    Store<BackPressedIntent, Unit, NavigationLabel.Exit> {

    sealed class BackPressedIntent {
        object OnBackPressed : BackPressedIntent()
    }

    companion object {
        val NAME = BackPressedStore::class.simpleName!!
    }
}

@FeatureScope
class CommonNavigationStoreExecutor @Inject constructor() :
    RxJavaExecutor<BackPressedIntent, Unit, Unit, Unit, NavigationLabel.Exit>() {

    override fun executeIntent(intent: BackPressedIntent, getState: () -> Unit) {
        when (intent) {
            BackPressedIntent.OnBackPressed -> publish(NavigationLabel.Exit)
        }
    }
}