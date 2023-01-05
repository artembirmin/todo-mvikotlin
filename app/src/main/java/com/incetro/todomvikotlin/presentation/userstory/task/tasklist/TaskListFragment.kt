/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.task.tasklist

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.incetro.todomvikotlin.R
import com.incetro.todomvikotlin.databinding.FragmentTaskListBinding
import com.incetro.todomvikotlin.presentation.base.fragment.BaseFragment
import com.incetro.todomvikotlin.presentation.userstory.task.di.TaskComponent
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class TaskListFragment : BaseFragment<FragmentTaskListBinding>(), TaskListView {

    override val layoutRes = R.layout.fragment_task_list
    private val taskListAdapter by lazy { TaskListAdapter() }

    @Inject
    @InjectPresenter
    lateinit var presenter: TaskListPresenter

    @ProvidePresenter
    fun providePresenter(): TaskListPresenter = presenter

    override fun inject() = TaskComponent.Manager.getComponent().inject(this)
    override fun release() = TaskComponent.Manager.releaseComponent()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        with(binding) {
            rvTaskList.layoutManager = GridLayoutManager(context, 2)
            rvTaskList.adapter = taskListAdapter
            val items = mutableListOf<TaskViewItem>().apply {
                repeat(10) {
                    add(TaskViewItem())
                }
            }
            taskListAdapter.items = items
        }
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    companion object {
        fun newInstance() = TaskListFragment()
    }
}