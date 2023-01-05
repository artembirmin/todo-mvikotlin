/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.common.di.app.module

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Cicerone.Companion.create
import com.github.terrakok.cicerone.NavigatorHolder
import com.incetro.todomvikotlin.common.navigation.AppRouter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppNavigationModule {
    @Provides
    @Singleton
    fun provideAppCicerone(): Cicerone<AppRouter> = create(AppRouter())

    @Provides
    @Singleton
    fun provideAppRouter(appCicerone: Cicerone<AppRouter>): AppRouter = appCicerone.router

    @Provides
    @Singleton
    fun provideNavigationHolder(appCicerone: Cicerone<AppRouter>): NavigatorHolder =
        appCicerone.getNavigatorHolder()
}