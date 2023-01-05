/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.task.di

import com.incetro.todomvikotlin.common.di.activity.ActivityComponent
import com.incetro.todomvikotlin.common.di.componentmanager.ComponentManager
import com.incetro.todomvikotlin.common.di.componentmanager.ComponentsManager
import com.incetro.todomvikotlin.common.di.scope.FeatureScope
import com.incetro.todomvikotlin.presentation.userstory.task.tasklist.TaskListFragment
import dagger.Component

@FeatureScope
@Component(
    dependencies = [ActivityComponent::class],
    modules = [
        TaskModule::class
    ]
)
interface TaskComponent {
    fun inject(tasksFragment: TaskListFragment)

    @Component.Builder
    interface Builder {
        fun activityComponent(component: ActivityComponent): Builder
        fun build(): TaskComponent
    }

    object Manager : ComponentManager<TaskComponent>(
        clazz = TaskComponent::class.java,
        createAndSave = {
            val componentManager = ComponentsManager.getInstance()
            val activityComponent = ActivityComponent.Manager.getComponent()
            val component = DaggerTaskComponent.builder()
                .activityComponent(activityComponent)
                .build()
            componentManager.addComponent(component)
        })
}