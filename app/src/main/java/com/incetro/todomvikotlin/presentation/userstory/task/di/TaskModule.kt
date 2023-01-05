/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.task.di

import com.incetro.todomvikotlin.common.di.scope.FeatureScope
import com.incetro.todomvikotlin.model.repository.TaskRepository
import com.incetro.todomvikotlin.model.repository.TaskRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class TaskModule {
    @Binds
    @FeatureScope
    abstract fun provideTaskRepository(taskRepositoryImpl: TaskRepositoryImpl): TaskRepository
}