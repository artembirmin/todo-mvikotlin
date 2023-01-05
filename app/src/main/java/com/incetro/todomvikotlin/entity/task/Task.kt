/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.entity.task

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.incetro.todomvikotlin.entity.task.Task.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val attemptsMadeCount: Int,
    val attemptsRequiredCount: Int
) {
    companion object {
        const val TABLE_NAME = "task"
    }
}
