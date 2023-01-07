/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.model.store.tasklist

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.incetro.todomvikotlin.common.di.scope.FeatureScope
import com.incetro.todomvikotlin.common.mvibase.CommonLabel
import com.incetro.todomvikotlin.common.mvirxjava.RxJavaExecutor
import com.incetro.todomvikotlin.entity.task.Task
import com.incetro.todomvikotlin.model.repository.TaskRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import kotlin.random.Random

typealias TodoListStore = Store<TaskStoreStoreFactory.Intent, TaskStoreStoreFactory.State, CommonLabel>

@FeatureScope
class TaskStoreStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val repository: TaskRepository
) {

    fun create(): TodoListStore =
        object : TodoListStore by storeFactory.create(
            name = "ListStore",
            bootstrapper = SimpleBootstrapper(Unit),
            initialState = State(),
            executorFactory = { ExecutorImpl() },
            reducer = ReducerImpl
        ) {
        }

    sealed class Intent {
        data class ShowTasks(val tasks: List<Task>) : Intent()
        object OnAddTaskClick : Intent()
    }

    data class State(
        val items: List<Task> = emptyList()
    )

    inner class ExecutorImpl :
        RxJavaExecutor<Intent, Unit, State, Intent, CommonLabel>() {
        override fun executeAction(action: Unit, getState: () -> State) {
            refreshTasks()
            observeTasks()
        }

        private fun refreshTasks() {
            repository.refreshTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { publish(CommonLabel.ShowLoading) }
                .doOnComplete { publish(CommonLabel.HideLoading) }
                .subscribeBy(
                    onError = { CommonLabel.ShowError(it) },
                )
                .addDisposable()
        }

        private fun observeTasks() {
            repository.observeTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { dispatch(Intent.ShowTasks(it)) },
                    onError = {},
                )
                .addDisposable()
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.OnAddTaskClick -> createNewRandomTask()
                else -> dispatch(intent)
            }
        }

        private fun createNewRandomTask() {
            repository.saveNewTask(
                Task(
                    name = Random.nextInt().toString(),
                    color = 0,
                    attemptsMadeCount = 0,
                    attemptsRequiredCount = 0
                )
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onError = {},
                )
                .addDisposable()
        }
    }

    object ReducerImpl : Reducer<State, Intent> {
        override fun State.reduce(msg: Intent): State =
            when (msg) {
                is Intent.ShowTasks -> copy(items = msg.tasks)
                else -> {
                    this
                }
            }
    }
}