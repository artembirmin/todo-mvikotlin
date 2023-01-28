/*
 * todomvikotlin
 *
 * Created by artembirmin on 10/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.task.tasklist

import android.os.Parcelable
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.incetro.todomvikotlin.common.mvicommon.CommonAction
import com.incetro.todomvikotlin.common.mvicommon.CommonLabel
import com.incetro.todomvikotlin.common.mvicommon.NavigationLabel
import com.incetro.todomvikotlin.common.mvirxjava.RxJavaExecutor
import com.incetro.todomvikotlin.common.navigation.Screens
import com.incetro.todomvikotlin.entity.task.Task
import com.incetro.todomvikotlin.model.repository.TaskRepository
import com.incetro.todomvikotlin.presentation.userstory.task.taskinfo.TaskInfoFragmentInitParams
import com.incetro.todomvikotlin.presentation.userstory.task.tasklist.TaskListStore.Intent
import com.incetro.todomvikotlin.presentation.userstory.task.tasklist.TaskListStore.Message
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.parcelize.Parcelize
import javax.inject.Inject
import kotlin.random.Random

abstract class TaskListStore : Store<Intent, TaskListStore.State, CommonLabel> {

    sealed class Intent : JvmSerializable {
        object OnAddTaskClick : Intent()
        data class OnTaskClick(val task: Task) : Intent()
        object OnBackPressed : Intent()
    }

    sealed class Message : JvmSerializable {
        data class ShowTasks(val tasks: List<Task>) : Message()
    }

    @Parcelize
    data class State(
        val items: List<Task> = emptyList(),
    ) : Parcelable, JvmSerializable

    companion object {
        val NAME = TaskListStore::class.simpleName!!
    }
}

class TaskListStoreReducer @Inject constructor() :
    Reducer<TaskListStore.State, Message> {
    override fun TaskListStore.State.reduce(msg: Message): TaskListStore.State =
        when (msg) {
            is Message.ShowTasks -> copy(items = msg.tasks)
        }
}

class TaskListStoreExecutor @Inject constructor(
    private val taskRepository: TaskRepository
) : RxJavaExecutor<Intent, CommonAction, TaskListStore.State, Message, CommonLabel>() {
    override fun executeAction(action: CommonAction, getState: () -> TaskListStore.State) {
        refreshTasks()
        observeTasks()
    }

    override fun executeIntent(intent: Intent, getState: () -> TaskListStore.State) {
        when (intent) {
            is Intent.OnAddTaskClick -> createNewRandomTask()
            is Intent.OnTaskClick -> publish(
                NavigationLabel.NavigateTo(
                    Screens.TaskInfoScreen(
                        TaskInfoFragmentInitParams(
                            intent.task.id
                        )
                    )
                )
            )
            is Intent.OnBackPressed -> publish(NavigationLabel.Exit)
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