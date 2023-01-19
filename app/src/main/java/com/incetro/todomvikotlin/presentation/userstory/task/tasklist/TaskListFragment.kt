/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.task.tasklist

import android.os.Bundle
import android.view.View
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.incetro.todomvikotlin.R
import com.incetro.todomvikotlin.common.mvibase.CommonLabel
import com.incetro.todomvikotlin.common.mvirxjava.bind
import com.incetro.todomvikotlin.common.mvirxjava.createStoreSimple
import com.incetro.todomvikotlin.common.mvirxjava.events
import com.incetro.todomvikotlin.common.mvirxjava.states
import com.incetro.todomvikotlin.databinding.FragmentTaskListBinding
import com.incetro.todomvikotlin.presentation.base.fragment.BaseFragment
import com.incetro.todomvikotlin.presentation.userstory.task.di.TaskComponent
import javax.inject.Inject

class TaskListFragment : BaseFragment<FragmentTaskListBinding>() {

    override val layoutRes = R.layout.fragment_task_list

    override lateinit var store: TaskListStore
    override val storeName = TaskListStore.NAME

    override fun inject() = TaskComponent.Manager.getComponent().inject(this)
    override fun release() = TaskComponent.Manager.releaseComponent()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mviView = TaskListView(binding)
        val lifecycle = viewLifecycleOwner.essentyLifecycle()

        bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
            mviView.events bindTo store
        }
        bind(lifecycle, BinderLifecycleMode.START_STOP) {
            store.states bindTo mviView
        }
    }

    @Inject
    fun createStore(
        storeFactory: StoreFactory,
        reducer: TaskListStoreReducer,
        executor: TaskListStoreExecutor
    ) {
        store = storeInstanceKeeper.getStore() as? TaskListStore
            ?: object : TaskListStore(),
                Store<TaskListStore.Intent, TaskListStore.State, CommonLabel>
                by storeFactory.createStoreSimple(
                    name = TaskListStore.NAME,
                    initialState = TaskListStore.State(),
                    reducer = reducer,
                    executor = executor
                ) {}
    }

    override fun onBackPressed() {
        router.exit()
    }

    companion object {
        fun newInstance() = TaskListFragment()
    }
}