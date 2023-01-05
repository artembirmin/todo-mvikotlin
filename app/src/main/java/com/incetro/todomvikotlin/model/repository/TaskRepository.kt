/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.model.repository

import com.incetro.todomvikotlin.entity.task.Task
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface TaskRepository {
    fun observeTasks(): Flowable<List<Task>>
    fun getTaskById(id: Int): Single<Task>
    fun saveNewTask(task: Task): Completable
}