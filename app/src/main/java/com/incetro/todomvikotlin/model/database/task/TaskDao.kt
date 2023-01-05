/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.model.database.task

import androidx.room.Dao
import androidx.room.Query
import com.incetro.todomvikotlin.entity.task.Task
import com.incetro.todomvikotlin.model.database.BaseDao
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface TaskDao : BaseDao<Task> {

    @Query("SELECT * FROM ${Task.TABLE_NAME} WHERE id = :id")
    fun getById(id: Int): Single<Task>

    @Query("SELECT * FROM ${Task.TABLE_NAME}")
    fun observe(): Flowable<List<Task>>
}