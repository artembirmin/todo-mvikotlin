/*
 * todomvikotlin
 *
 * Created by artembirmin on 10/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.task.taskinfo

import android.os.Parcelable
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.incetro.todomvikotlin.common.mvicommon.CommonAction
import com.incetro.todomvikotlin.common.mvicommon.CommonLabel
import com.incetro.todomvikotlin.common.mvicommon.NavigationLabel
import com.incetro.todomvikotlin.common.mvirxjava.RxJavaExecutor
import com.incetro.todomvikotlin.entity.task.Task
import com.incetro.todomvikotlin.model.repository.TaskRepository
import com.incetro.todomvikotlin.presentation.userstory.task.taskinfo.TaskInfoStore.Intent
import com.incetro.todomvikotlin.presentation.userstory.task.taskinfo.TaskInfoStore.Message
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import javax.inject.Inject

abstract class TaskInfoStore : Store<Intent, TaskInfoStore.State, CommonLabel> {

    companion object {
        val NAME = TaskInfoStore::class.simpleName!!
    }

    sealed class Intent : JvmSerializable {
        object OnBackPressed : Intent()
    }

    sealed class Message : JvmSerializable {
        data class ShowTask(val task: Task) : Message()
    }

    @Parcelize
    data class State(
        val taskId: Int,
        val task: Task = Task()
    ) : Parcelable, JvmSerializable
}

class TaskInfoStoreReducer @Inject constructor() : Reducer<TaskInfoStore.State, Message> {
    override fun TaskInfoStore.State.reduce(msg: Message): TaskInfoStore.State =
        when (msg) {
            is Message.ShowTask -> copy(task = msg.task)
        }
}

class TaskInfoStoreExecutor @Inject constructor(
    private val taskRepository: TaskRepository
) : RxJavaExecutor<Intent, CommonAction, TaskInfoStore.State, Message, CommonLabel>() {
    override fun executeAction(action: CommonAction, getState: () -> TaskInfoStore.State) {
        Timber.d("getTask executeAction")
        getTask(taskId = getState().taskId)
    }

    override fun executeIntent(intent: Intent, getState: () -> TaskInfoStore.State) {
        when (intent) {
            is Intent.OnBackPressed -> publish(NavigationLabel.Exit)
        }
    }

    private fun getTask(taskId: Int) {
        taskRepository.getTaskById(taskId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { Timber.d("getTask doOnSuccess") }
            .doOnSubscribe { Timber.d("getTask doOnSubscribe") }
            .subscribeBy(
                onSuccess = { dispatch(Message.ShowTask(it)) },
                onError = { publish(CommonLabel.ShowError(it)) },
            )
            .addDisposable()
    }
}