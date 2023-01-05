/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

@file:Suppress("FunctionName")

package com.incetro.todomvikotlin.common.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.incetro.todomvikotlin.presentation.userstory.demouserstory.demoscreen.DemoFragment

/**
 * App screens for navigation with Cicerone.
 */
object Screens {
    fun DemoScreen(): FragmentScreen =
        FragmentScreen("DemoScreen") {
            DemoFragment.newInstance()
        }
}