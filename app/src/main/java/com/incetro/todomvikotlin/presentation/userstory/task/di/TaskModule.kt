/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.task.di

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeperDispatcher
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arkivanov.mvikotlin.timetravel.store.TimeTravelStoreFactory
import com.incetro.todomvikotlin.BuildConfig
import com.incetro.todomvikotlin.common.di.scope.FeatureScope
import com.incetro.todomvikotlin.common.mvibase.NavigationLabel
import com.incetro.todomvikotlin.model.repository.TaskRepository
import com.incetro.todomvikotlin.model.repository.TaskRepositoryImpl
import com.incetro.todomvikotlin.presentation.base.store.CommonNavigationStoreExecutor
import com.incetro.todomvikotlin.presentation.base.store.NavigationStore
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
        fun provideStoreInstanceKeeper(): InstanceKeeper {
            return InstanceKeeperDispatcher()
        }

        @Provides
        @FeatureScope
        fun provideCommonNavigationStore(
            storeFactory: StoreFactory,
            commonNavigationStoreExecutor: CommonNavigationStoreExecutor
        ): NavigationStore {
            return object : NavigationStore(),
                Store<NavigationStore.NavigationIntent, Unit, NavigationLabel>
                by storeFactory.create(
                    name = NavigationStore.NAME,
                    executorFactory = { commonNavigationStoreExecutor },
                    initialState = Unit
                ) {}
        }
    }
}