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
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.incetro.todomvikotlin.R
import com.incetro.todomvikotlin.common.mvicommon.CommonLabel
import com.incetro.todomvikotlin.common.mvirxjava.bind
import com.incetro.todomvikotlin.common.mvirxjava.createStoreSimple
import com.incetro.todomvikotlin.common.mvirxjava.events
import com.incetro.todomvikotlin.common.mvirxjava.states
import com.incetro.todomvikotlin.databinding.FragmentTaskListBinding
import com.incetro.todomvikotlin.presentation.base.fragment.BaseStoreFragment
import com.incetro.todomvikotlin.presentation.userstory.task.di.TaskComponent
import com.incetro.todomvikotlin.presentation.userstory.task.tasklist.TaskListStore.Intent
import com.incetro.todomvikotlin.presentation.userstory.task.tasklist.TaskListStore.State
import timber.log.Timber
import javax.inject.Inject

class TaskListFragment : BaseStoreFragment<FragmentTaskListBinding>() {

    override val layoutRes = R.layout.fragment_task_list

    override lateinit var view: TaskListView

    override lateinit var store: TaskListStore
    override val storeName = TaskListStore.NAME

    override fun inject() = TaskComponent.Manager.getComponent().inject(this)
    override fun release() = TaskComponent.Manager.releaseComponent()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMvi()
    }

    private fun initMvi() {
        Timber.tag("SAVE_STATE_TEST")
            .d("initMvi TaskInfo store hash ${store.hashCode()}")
        view = TaskListView(binding)
        val lifecycle = viewLifecycleOwner.essentyLifecycle()

        bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
            view.events bindTo store
        }
        bind(lifecycle, BinderLifecycleMode.START_STOP) {
            store.states bindTo view
        }
    }

    @Inject
    fun createStore(
        storeFactory: StoreFactory,
        reducer: TaskListStoreReducer,
        executor: TaskListStoreExecutor
    ) {
        store = storeInstanceKeeper.getStore(key = storeName) {
            object : TaskListStore(),
                Store<Intent, State, CommonLabel>
                by storeFactory.createStoreSimple(
                    name = storeName,
                    initialState = getState() ?: State(),
                    reducer = reducer,
                    executor = executor
                ) {}
                .also {
                    stateKeeper.register(key = storeName) {
                        Timber.tag("SAVE_STATE_TEST").d(" stateKeeper.register")
                        it.state
                    }
                }
        }
    }

    companion object {
        fun newInstance() = TaskListFragment()
    }
}