/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

@file:Suppress("FunctionName")

package com.incetro.todomvikotlin.common.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.incetro.todomvikotlin.presentation.userstory.task.taskinfo.TaskInfoFragment
import com.incetro.todomvikotlin.presentation.userstory.task.taskinfo.TaskInfoFragmentInitParams
import com.incetro.todomvikotlin.presentation.userstory.task.tasklist.TaskListFragment

/**
 * App screens for navigation with Cicerone.
 */
object Screens {
    fun TaskListScreen(): FragmentScreen =
        object : FragmentScreen, JvmSerializable {
            override val screenKey: String = "TaskListScreen"
            override fun createFragment(factory: FragmentFactory): Fragment {
                return TaskListFragment.newInstance()
            }
        }

    fun TaskInfoScreen(initParams: TaskInfoFragmentInitParams): FragmentScreen {
        return object : FragmentScreen, JvmSerializable {
            override val screenKey: String = "TaskInfoScreen"
            override fun createFragment(factory: FragmentFactory): Fragment {
                return TaskInfoFragment.newInstance(initParams)
            }
        }
    }
}