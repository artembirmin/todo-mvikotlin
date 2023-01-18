/*
 * todomvikotlin
 *
 * Created by artembirmin on 10/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.task.taskinfo

import com.arkivanov.mvikotlin.core.utils.diff
import com.arkivanov.mvikotlin.core.view.BaseMviView
import com.arkivanov.mvikotlin.core.view.ViewRenderer
import com.incetro.todomvikotlin.databinding.FragmentTaskInfoBinding
import com.incetro.todomvikotlin.presentation.userstory.task.taskinfo.TaskInfoStore.Intent
import com.incetro.todomvikotlin.presentation.userstory.task.taskinfo.TaskInfoStore.State

class TaskInfoView(binding: FragmentTaskInfoBinding) :
    BaseMviView<State, Intent>() {

    override val renderer: ViewRenderer<State> =
        diff {
            this.diff(
                get = State::task,
                set = { task ->
                    with(binding) {
                        tvTaskName.text = task.name
                    }
                }
            )
        }
}