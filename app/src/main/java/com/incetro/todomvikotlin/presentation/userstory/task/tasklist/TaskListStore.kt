/*
 * todomvikotlin
 *
 * Created by artembirmin on 10/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.task.tasklist

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.incetro.todomvikotlin.common.di.scope.FeatureScope
import com.incetro.todomvikotlin.common.mvibase.CommonLabel
import com.incetro.todomvikotlin.common.mvibase.NavigationLabel
import com.incetro.todomvikotlin.common.mvirxjava.RxJavaExecutor
import com.incetro.todomvikotlin.common.navigation.Screens
import com.incetro.todomvikotlin.entity.task.Task
import com.incetro.todomvikotlin.model.repository.TaskRepository
import com.incetro.todomvikotlin.presentation.userstory.task.taskinfo.TaskInfoFragmentInitParams
import com.incetro.todomvikotlin.presentation.userstory.task.tasklist.TaskListStore.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import kotlin.random.Random

abstract class TaskListStore : Store<Intent, State, CommonLabel> {

    sealed class Intent {
        object OnAddTaskClick : Intent()
        data class OnTaskClick(val task: Task) : Intent()
    }

    sealed class Message {
        data class ShowTasks(val tasks: List<Task>) : Message()
    }

    data class State(
        val items: List<Task> = emptyList()
    )

    companion object {
        val NAME = TaskListStore::class.simpleName
    }
}

@FeatureScope
class TaskListStoreReducer @Inject constructor() : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State =
        when (msg) {
            is Message.ShowTasks -> copy(items = msg.tasks)
        }
}

@FeatureScope
class TaskListStoreExecutor @Inject constructor(
    private val taskRepository: TaskRepository
) : RxJavaExecutor<Intent, Unit, State, Message, CommonLabel>() {
    override fun executeAction(action: Unit, getState: () -> State) {
        refreshTasks()
        observeTasks()
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.OnAddTaskClick -> createNewRandomTask()
            is Intent.OnTaskClick -> publish(
                NavigationLabel.NavigateTo(
                    Screens.TaskInfoScreen(
                        TaskInfoFragmentInitParams(intent.task.id)
                    )
                )
            )
        }
    }

    private fun refreshTasks() {
        taskRepository.refreshTasks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                publish(CommonLabel.ShowLoading)
            }
            .doOnComplete {
                publish(CommonLabel.HideLoading)
            }
            .subscribeBy(
                onError = {
                    publish(CommonLabel.ShowError(it))
                },
            )
            .addDisposable()
    }

    private fun observeTasks() {
        taskRepository.observeTasks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { dispatch(Message.ShowTasks(it)) },
                onError = { publish(CommonLabel.ShowError(it)) },
            )
            .addDisposable()
    }

    private fun createNewRandomTask() {
        taskRepository.saveNewTask(
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
                onError = { publish(CommonLabel.ShowError(it)) },
            )
            .addDisposable()
    }
}