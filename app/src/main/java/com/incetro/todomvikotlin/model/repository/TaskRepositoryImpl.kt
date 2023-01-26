/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.model.repository

import com.incetro.todomvikotlin.common.di.scope.FeatureScope
import com.incetro.todomvikotlin.entity.task.Task
import com.incetro.todomvikotlin.model.database.task.TaskDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@FeatureScope
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    override fun observeTasks(): Flowable<List<Task>> {
        return taskDao.observe()
    }

    override fun refreshTasks(): Completable {
        return Completable.timer(2000, TimeUnit.MILLISECONDS)
            .concatWith(Completable.error(Exception("MVP был лучше")))
    }

    override fun getTaskById(id: Int): Single<Task> {
        return taskDao.getById(id)
    }

    override fun saveNewTask(task: Task): Completable {
        Timber.tag("SAVE_STATE_TEST")
            .d("TaskRepositoryImpl saveNewTask hashCode ${this.hashCode()}")
        return taskDao.insert(task)
    }
}
