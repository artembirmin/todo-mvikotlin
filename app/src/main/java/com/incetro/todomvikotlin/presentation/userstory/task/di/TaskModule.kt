/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.task.di

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arkivanov.mvikotlin.timetravel.store.TimeTravelStoreFactory
import com.incetro.todomvikotlin.BuildConfig
import com.incetro.todomvikotlin.common.di.scope.FeatureScope
import com.incetro.todomvikotlin.common.mvibase.CommonLabel
import com.incetro.todomvikotlin.model.repository.TaskRepository
import com.incetro.todomvikotlin.model.repository.TaskRepositoryImpl
import com.incetro.todomvikotlin.model.store.tasklist.TaskListStore
import com.incetro.todomvikotlin.model.store.tasklist.TaskListStoreExecutor
import com.incetro.todomvikotlin.model.store.tasklist.TaskListStoreReducer
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class TaskModule {
    @Binds
    @FeatureScope
    abstract fun provideTaskRepository(taskRepositoryImpl: TaskRepositoryImpl): TaskRepository

    companion object {
        @Provides
        @FeatureScope
        fun provideStoreFactory(): StoreFactory {
            return if (BuildConfig.DEBUG)
                LoggingStoreFactory(delegate = TimeTravelStoreFactory())
            else DefaultStoreFactory()
        }

        @Provides
        @FeatureScope
        fun provideTaskListStore(
            storeFactory: StoreFactory,
            executor: TaskListStoreExecutor,
            reducer: TaskListStoreReducer
        ): TaskListStore {
            return object : TaskListStore,
                Store<TaskListStore.Intent, TaskListStore.State, CommonLabel> by storeFactory.create(
                    autoInit = false,
                    name = "TaskListStore",
                    bootstrapper = SimpleBootstrapper(Unit),
                    initialState = TaskListStore.State(),
                    executorFactory = { executor },
                    reducer = reducer
                ) {}
        }
    }
}