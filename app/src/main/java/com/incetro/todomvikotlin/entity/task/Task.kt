/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.entity.task

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.incetro.todomvikotlin.entity.task.Task.Companion.TABLE_NAME
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = TABLE_NAME
)
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val attemptsMadeCount: Int = 0,
    val attemptsRequiredCount: Int = 0,
    @ColorRes val color: Int = 0
) : Parcelable {
    companion object {
        const val TABLE_NAME = "task"
    }
}
