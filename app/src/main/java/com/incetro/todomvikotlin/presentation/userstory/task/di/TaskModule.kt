/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.task.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.incetro.todomvikotlin.BuildConfig
import com.incetro.todomvikotlin.common.di.scope.FeatureScope
import com.incetro.todomvikotlin.model.repository.TaskRepository
import com.incetro.todomvikotlin.model.repository.TaskRepositoryImpl
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
                LoggingStoreFactory(delegate = DefaultStoreFactory())
            else DefaultStoreFactory()
        }
    }
}