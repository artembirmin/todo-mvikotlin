/*
 * todomvikotlin
 *
 * Created by artembirmin on 10/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.task.taskinfo

import android.os.Bundle
import android.view.View
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.incetro.todomvikotlin.R
import com.incetro.todomvikotlin.common.mvibase.CommonLabel
import com.incetro.todomvikotlin.common.mvirxjava.bind
import com.incetro.todomvikotlin.common.mvirxjava.createStoreSimple
import com.incetro.todomvikotlin.common.mvirxjava.events
import com.incetro.todomvikotlin.common.mvirxjava.states
import com.incetro.todomvikotlin.common.navigation.getInitParams
import com.incetro.todomvikotlin.common.navigation.provideInitParams
import com.incetro.todomvikotlin.databinding.FragmentTaskInfoBinding
import com.incetro.todomvikotlin.presentation.base.fragment.BaseStoreFragment
import com.incetro.todomvikotlin.presentation.userstory.task.di.TaskComponent
import com.incetro.todomvikotlin.presentation.userstory.task.taskinfo.TaskInfoStore.Intent
import com.incetro.todomvikotlin.presentation.userstory.task.taskinfo.TaskInfoStore.State
import timber.log.Timber
import javax.inject.Inject


class TaskInfoFragment : BaseStoreFragment<FragmentTaskInfoBinding>() {

    override val layoutRes = R.layout.fragment_task_info

    private val initParams by lazy { getInitParams<TaskInfoFragmentInitParams>() }

    override lateinit var store: TaskInfoStore
    override val storeName = TaskInfoStore.NAME

    override fun inject() = TaskComponent.Manager.getComponent().inject(this)
    override fun release() = Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMvi()
    }

    // Pull up
    private fun initMvi() {
        Timber.tag("SAVE_STATE_TEST")
            .d("initMvi TaskInfo store hash ${store.hashCode()}")
        val mviView = TaskInfoView(binding)
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
        reducer: TaskInfoStoreReducer,
        executor: TaskInfoStoreExecutor
    ) {
        store = storeInstanceKeeper.getStore(key = storeName) {
            object : TaskInfoStore(),
                Store<Intent, State, CommonLabel> by storeFactory.createStoreSimple(
                    name = storeName,
                    initialState = State(taskId = initParams.id),
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

    override fun onBackPressed() {
        router.exit()
    }

    companion object {
        fun newInstance(initParams: TaskInfoFragmentInitParams) =
            TaskInfoFragment().provideInitParams(initParams) as TaskInfoFragment
    }
}