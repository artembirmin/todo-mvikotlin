/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

@file:Suppress("FunctionName")

package com.incetro.todomvikotlin.common.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.incetro.todomvikotlin.presentation.userstory.task.taskinfo.TaskInfoFragment
import com.incetro.todomvikotlin.presentation.userstory.task.taskinfo.TaskInfoFragmentInitParams
import com.incetro.todomvikotlin.presentation.userstory.task.tasklist.TaskListFragment

/**
 * App screens for navigation with Cicerone.
 */
object Screens {
    fun TaskListScreen(): FragmentScreen =
        FragmentScreen("TaskListScreen") {
            TaskListFragment.newInstance()
        }

    fun TaskInfoScreen(initParams: TaskInfoFragmentInitParams): FragmentScreen =
        FragmentScreen("TaskInfoScreen") {
            TaskInfoFragment.newInstance(initParams)
        }
}