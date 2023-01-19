/*
 * todomvikotlin
 *
 * Created by artembirmin on 10/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.task.taskinfo

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.incetro.todomvikotlin.common.di.scope.FeatureScope
import com.incetro.todomvikotlin.common.mvibase.CommonLabel
import com.incetro.todomvikotlin.common.mvirxjava.RxJavaExecutor
import com.incetro.todomvikotlin.entity.task.Task
import com.incetro.todomvikotlin.model.repository.TaskRepository
import com.incetro.todomvikotlin.presentation.userstory.task.taskinfo.TaskInfoStore.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

abstract class TaskInfoStore : Store<Intent, State, CommonLabel> {

    companion object {
        val NAME = TaskInfoStore::class.simpleName!!
    }

    sealed class Intent {

    }

    sealed class Message {
        data class ShowTask(val task: Task) : Message()
    }

    data class State(
        val taskId: Int,
        val task: Task = Task()
    )
}

@FeatureScope
class TaskInfoStoreReducer @Inject constructor() : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State =
        when (msg) {
            is Message.ShowTask -> copy(task = msg.task)
        }
}

class TaskInfoStoreExecutor @Inject constructor(
    private val taskRepository: TaskRepository
) : RxJavaExecutor<Intent, Unit, State, Message, CommonLabel>() {
    override fun executeAction(action: Unit, getState: () -> State) {
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