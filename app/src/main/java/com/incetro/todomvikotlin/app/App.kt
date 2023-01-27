/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.app

import android.app.Application
import com.arkivanov.mvikotlin.timetravel.server.TimeTravelServer
import com.incetro.todomvikotlin.BuildConfig
import com.incetro.todomvikotlin.common.di.componentmanager.ComponentsManager
import timber.log.Timber

class App : Application() {
    private val timeTravelServer = TimeTravelServer()
    override fun onCreate() {
        Timber.tag("SAVE_STATE_TEST").d("Application onCreate")
        super.onCreate()
        timeTravelServer.start()
        inject()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        Timber.tag("SAVE_STATE_TEST").d("Application onTerminate")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Timber.tag("SAVE_STATE_TEST").d("Application onLowMemory")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Timber.tag("SAVE_STATE_TEST").d("Application onTrimMemory")

    }

    private fun inject() {
        ComponentsManager.init(this)
        ComponentsManager.getInstance().applicationComponent.inject(this)
    }
}