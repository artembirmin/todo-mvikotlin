/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.common.di.app.module

import android.app.Application
import android.content.Context
import com.incetro.todomvikotlin.app.AppLauncher
import com.incetro.todomvikotlin.common.navigation.AppRouter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Application) {

    @Provides
    @Singleton
    internal fun provideContext(): Context {
        return context.applicationContext
    }

    @Provides
    @Singleton
    fun provideAppLauncher(
        router: AppRouter,
    ): AppLauncher {
        return AppLauncher(router)
    }
}