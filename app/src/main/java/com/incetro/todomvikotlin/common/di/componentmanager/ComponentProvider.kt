/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.common.di.componentmanager

/**
 * Provides Dagger component.
 */
interface ComponentProvider {

    /**
     * Returns [clazz] instance. [clazz] is Dagger component class.
     */
    fun <T> getComponent(clazz: Class<T>): T
}