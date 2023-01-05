/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.common.di.activity

import android.content.Context
import com.incetro.todomvikotlin.app.AppActivity
import com.incetro.todomvikotlin.common.di.app.AppComponent
import com.incetro.todomvikotlin.common.di.componentmanager.ComponentManager
import com.incetro.todomvikotlin.common.di.componentmanager.ComponentsManager
import com.incetro.todomvikotlin.common.di.scope.ActivityScope
import com.incetro.todomvikotlin.common.manager.ResourcesManager
import com.incetro.todomvikotlin.common.navigation.AppRouter
import com.incetro.todomvikotlin.model.database.AppDatabase
import com.incetro.todomvikotlin.model.database.demo.DemoDao
import dagger.Component

@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = [
        ActivityModule::class
    ]
)
interface ActivityComponent {
    fun inject(appActivity: AppActivity)

    // AppModule
    fun provideContext(): Context

    // AppNavigationModule from AppComponent
    fun provideAppRouter(): AppRouter

    // Database module
    fun provideAppDatabase(): AppDatabase
    fun provideDemoDao(): DemoDao

    // Other
    fun provideResourcesManager(): ResourcesManager

    @Component.Builder
    interface Builder {
        fun appComponent(component: AppComponent): Builder
        fun activityModule(activityModule: ActivityModule): Builder
        fun build(): ActivityComponent
    }

    object Manager : ComponentManager<ActivityComponent>(
        clazz = ActivityComponent::class.java,
        createAndSave = {
            val componentManager = ComponentsManager.getInstance()
            val activityComponent = DaggerActivityComponent.builder()
                .appComponent(componentManager.applicationComponent)
                .activityModule(ActivityModule())
                .build()

            componentManager.addComponent(activityComponent)
        })
}