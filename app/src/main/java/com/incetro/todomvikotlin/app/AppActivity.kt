/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.app

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.incetro.todomvikotlin.BuildConfig
import com.incetro.todomvikotlin.R
import com.incetro.todomvikotlin.common.di.activity.ActivityComponent
import com.incetro.todomvikotlin.presentation.base.fragment.BaseFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

class AppActivity : AppCompatActivity() {

    @Inject
    lateinit var appLauncher: AppLauncher

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    /**
     * Instance of currently displayed fragment
     */
    private val currentFragment: BaseFragment<*>?
        get() = supportFragmentManager.findFragmentById(R.id.fragment_container) as? BaseFragment<*>

    private val navigator: Navigator = AppNavigator(this, R.id.fragment_container)

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.tag("SAVE_STATE_TEST").d("Activity onCreate")
        Timber.tag("SAVE_STATE_TEST").d(
            "Activity onCreate is savedInstanceState null -" +
                    " ${savedInstanceState == null}"
        )
        inject()
        super.onCreate(savedInstanceState)
        setContentView(
            if (BuildConfig.DEBUG) {
                R.layout.layout_container_debug
            } else {
                R.layout.layout_container
            }
        )

        if (savedInstanceState == null) {
            appLauncher.start()
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        Timber.tag("SAVE_STATE_TEST").d("Activity onSaveInstanceState1")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.tag("SAVE_STATE_TEST").d("Activity onSaveInstanceState2")
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        Timber.tag("SAVE_STATE_TEST").d("Activity onRestoreInstanceState1")
        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Timber.tag("SAVE_STATE_TEST").d("Activity onRestoreInstanceState2")
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun inject() {
        ActivityComponent.Manager.getComponent().inject(this)
    }

    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }

    override fun onDestroy() {
        Timber.tag("SAVE_STATE_TEST").d("Activity onDestroy")
        super.onDestroy()
        if (isFinishing) {
            compositeDisposable.clear()
            ActivityComponent.Manager.releaseComponent()
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    protected fun Disposable.addDisposable(): Disposable {
        compositeDisposable.add(this)
        return this
    }
}