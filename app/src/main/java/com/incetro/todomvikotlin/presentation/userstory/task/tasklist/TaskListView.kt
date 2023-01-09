/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.task.tasklist

import androidx.recyclerview.widget.GridLayoutManager
import com.arkivanov.mvikotlin.core.utils.diff
import com.arkivanov.mvikotlin.core.view.BaseMviView
import com.arkivanov.mvikotlin.core.view.ViewRenderer
import com.incetro.todomvikotlin.databinding.FragmentTaskListBinding
import com.incetro.todomvikotlin.model.store.tasklist.TaskListStore.Intent
import com.incetro.todomvikotlin.model.store.tasklist.TaskListStore.State


class TaskListView(binding: FragmentTaskListBinding) :
    BaseMviView<State, Intent>() {

    private val taskListAdapter by lazy { TaskListAdapter() }

    init {
        val context = binding.root.context
        with(binding) {
            rvTaskList.layoutManager = GridLayoutManager(context, 2)
            rvTaskList.adapter = taskListAdapter

            btnAddTask.setOnClickListener { dispatch(Intent.OnAddTaskClick) }
        }
    }

    override val renderer: ViewRenderer<State> =
        diff {
            this.diff(
                get = State::items,
                compare = { a, b -> a === b },
                set = { taskList ->
                    taskListAdapter.items = taskList.map {
                        TaskViewItem(
                            task = it,
                            onTaskClick = { task -> dispatch(Intent.OnTaskClick(task)) })
                    }.toMutableList()
                }
            )
        }
}
