/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.common.di.app

import android.content.Context
import com.github.terrakok.cicerone.NavigatorHolder
import com.incetro.todomvikotlin.app.App
import com.incetro.todomvikotlin.app.AppLauncher
import com.incetro.todomvikotlin.common.di.app.module.AppModule
import com.incetro.todomvikotlin.common.di.app.module.AppNavigationModule
import com.incetro.todomvikotlin.common.di.app.module.CommonAppModule
import com.incetro.todomvikotlin.common.di.app.module.DatabaseModule
import com.incetro.todomvikotlin.common.manager.ResourcesManager
import com.incetro.todomvikotlin.common.navigation.AppRouter
import com.incetro.todomvikotlin.model.database.AppDatabase
import com.incetro.todomvikotlin.model.database.task.TaskDao
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        CommonAppModule::class,
        AppNavigationModule::class,
        DatabaseModule::class,
    ]
)
interface AppComponent {

    fun inject(app: App)

    // AppModule
    fun provideContext(): Context
    fun provideAppLauncher(): AppLauncher

    // CommonAppModule

    // AppNavigationModule
    fun provideNavigationHolder(): NavigatorHolder
    fun provideAppRouter(): AppRouter

    // Database module
    fun provideAppDatabase(): AppDatabase
    fun provideDemoDao(): TaskDao

    // Other
    fun provideResourcesManager(): ResourcesManager

    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }
}