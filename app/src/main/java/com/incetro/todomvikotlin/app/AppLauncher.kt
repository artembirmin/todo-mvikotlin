/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.app

import com.incetro.todomvikotlin.common.navigation.AppRouter
import com.incetro.todomvikotlin.common.navigation.Screens

class AppLauncher(
    private val router: AppRouter,
) {
    /**
     *  Initialized and launches application.
     */
    fun start() {
        router.newRootScreen(Screens.DemoScreen())
    }
}