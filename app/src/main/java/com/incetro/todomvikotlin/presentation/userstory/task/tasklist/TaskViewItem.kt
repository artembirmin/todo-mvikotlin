/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.task.tasklist

import com.incetro.todomvikotlin.R
import com.incetro.todomvikotlin.databinding.ItemTaskBinding
import com.incetro.todomvikotlin.entity.task.Task
import com.incetro.todomvikotlin.presentation.base.adapter.DataBindingViewHolder
import com.incetro.todomvikotlin.presentation.base.adapter.DataBindingViewItem

class TaskViewItem(
    private val task: Task,
    private val onTaskClick: (Task) -> Unit
) : DataBindingViewItem<ItemTaskBinding> {

    override fun getLayoutId() = R.layout.item_task

    override fun bind(holder: DataBindingViewHolder<ItemTaskBinding>) {
        with(holder.binding) {
            root.setOnClickListener { onTaskClick(task) }

            tvTaskName.text = task.name
        }
    }
}