/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.incetro.todomvikotlin.BuildConfig
import com.incetro.todomvikotlin.entity.task.Task
import com.incetro.todomvikotlin.model.database.task.TaskDao

@Database(
    entities = [
        Task::class,
    ],
    version = AppDatabase.VERSION
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = BuildConfig.DB_NAME
        const val VERSION = 1
    }

    abstract fun taskDao(): TaskDao
}