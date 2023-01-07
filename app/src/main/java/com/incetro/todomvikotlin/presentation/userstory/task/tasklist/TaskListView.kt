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
import com.incetro.todomvikotlin.model.store.tasklist.TaskStoreStoreFactory


class TaskListView(binding: FragmentTaskListBinding) :
    BaseMviView<TaskStoreStoreFactory.State, TaskStoreStoreFactory.Intent>() {

    private val taskListAdapter by lazy { TaskListAdapter() }

    override val renderer: ViewRenderer<TaskStoreStoreFactory.State> =
        diff {
            diff(
                get = TaskStoreStoreFactory.State::items,
                compare = { a, b -> a === b },
                set = { taskList ->
                    taskListAdapter.items = taskList.map {
                        TaskViewItem(task = it, onTaskClick = { task -> })
                    }.toMutableList()
                }
            )
        }

    init {
        val context = binding.root.context
        with(binding) {
            rvTaskList.layoutManager = GridLayoutManager(context, 2)
            rvTaskList.adapter = taskListAdapter

            btnAddTask.setOnClickListener { dispatch(TaskStoreStoreFactory.Intent.OnAddTaskClick) }
        }
    }
}
