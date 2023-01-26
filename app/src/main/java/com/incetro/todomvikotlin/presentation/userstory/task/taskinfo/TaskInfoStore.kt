/*
 * todomvikotlin
 *
 * Created by artembirmin on 10/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.task.taskinfo

import android.os.Parcelable
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.incetro.todomvikotlin.common.mvibase.CommonLabel
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

    sealed class Intent {

    }

    sealed class Message {
        data class ShowTask(val task: Task) : Message()
    }

    @Parcelize
    data class State(
        val taskId: Int,
        val task: Task = Task()
    ) : Parcelable
}

class TaskInfoStoreReducer @Inject constructor() : Reducer<TaskInfoStore.State, Message> {
    override fun TaskInfoStore.State.reduce(msg: Message): TaskInfoStore.State =
        when (msg) {
            is Message.ShowTask -> copy(task = msg.task)
        }
}

class TaskInfoStoreExecutor @Inject constructor(
    private val taskRepository: TaskRepository
) : RxJavaExecutor<Intent, Unit, TaskInfoStore.State, Message, CommonLabel>() {
    override fun executeAction(action: Unit, getState: () -> TaskInfoStore.State) {
        Timber.d("getTask executeAction")
        getTask(taskId = getState().taskId)
    }

//    override fun executeIntent(intent: Intent, getState: () -> State) {
//        when (intent) {
//            else -> {}
//        }
//    }

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