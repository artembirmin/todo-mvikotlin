/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.model.database.demo

import androidx.room.Dao
import androidx.room.Query
import com.incetro.todomvikotlin.model.database.BaseDao
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface DemoDao : BaseDao<DemoDto> {

    @Query("SELECT * FROM ${DemoDto.TABLE_NAME} WHERE id = :id")
    fun getDemo(id: String): Single<DemoDto>

    @Query("SELECT * FROM ${DemoDto.TABLE_NAME} WHERE id = :id")
    fun observeDemo(id: String): Flowable<DemoDto>

    @Query("SELECT EXISTS(SELECT * FROM ${DemoDto.TABLE_NAME} WHERE id = :id)")
    fun isDemoExist(id: String): Boolean
}