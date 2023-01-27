/*
 * todomvikotlin
 *
 * Created by artembirmin on 26/1/2023.
 */

package com.incetro.todomvikotlin.presentation.base.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.databinding.ViewDataBinding
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.essenty.statekeeper.stateKeeper
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.view.ViewEvents
import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.rx.Observer
import com.arkivanov.mvikotlin.rx.internal.PublishSubject
import com.incetro.todomvikotlin.common.mvibase.CommonLabel
import com.incetro.todomvikotlin.common.mvibase.NavigationLabel
import com.incetro.todomvikotlin.common.mvirxjava.bind
import com.incetro.todomvikotlin.common.mvirxjava.events
import com.incetro.todomvikotlin.common.mvirxjava.labels
import com.incetro.todomvikotlin.presentation.base.store.NavigationStore
import com.incetro.todomvikotlin.presentation.base.store.NavigationStore.NavigationIntent
import com.incetro.todomvikotlin.utils.ext.mvi.removeStore
import javax.inject.Inject

abstract class BaseStoreFragment<Binding : ViewDataBinding> : BaseFragment<Binding>(),
    ViewEvents<NavigationIntent> {

    abstract val store: Store<*, *, CommonLabel>

    abstract val storeName: String

    protected val stateKeeper by lazy { stateKeeper() }

    @Inject
    lateinit var storeInstanceKeeper: InstanceKeeper

    @Inject
    lateinit var navigationStore: NavigationStore
    private val navigationStoreSubject = PublishSubject<NavigationIntent>()
    override fun events(observer: Observer<NavigationIntent>): Disposable =
        navigationStoreSubject.subscribe(observer)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lifecycle = viewLifecycleOwner.essentyLifecycle()

        bind(lifecycle, BinderLifecycleMode.START_STOP) {
            this@BaseStoreFragment.events bindTo navigationStore
        }
        bind(lifecycle, BinderLifecycleMode.START_STOP) {
            navigationStore.labels bindTo ::handleNavigationLabel
        }

        bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
            store.labels bindTo ::handleCommonLabel
        }

        lifecycle.doOnCreate {
            store.init()
        }
    }

    private fun handleCommonLabel(commonLabel: CommonLabel) {
        when (commonLabel) {
            is NavigationLabel -> handleNavigationLabel(commonLabel)
            is CommonLabel.ShowMessageByToast -> showMessageByToast(commonLabel.message)
            is CommonLabel.HideLoading -> hideProgress()
            is CommonLabel.ShowLoading -> showProgress()
            is CommonLabel.ShowError -> showError(commonLabel.error)
        }
    }

    private fun handleNavigationLabel(navigationLabel: NavigationLabel) {
        when (navigationLabel) {
            is NavigationLabel.BackTo -> router.backTo(navigationLabel.screen)
            is NavigationLabel.Exit -> router.exit()
            is NavigationLabel.NavigateTo -> router.navigateTo(navigationLabel.screen)
            is NavigationLabel.NewRootScreen -> router.newRootScreen(navigationLabel.screen)
            is NavigationLabel.ReplaceScreen -> router.replaceScreen(navigationLabel.screen)
        }
    }

    protected inline fun <reified T : Parcelable> getState(): T? {
        return stateKeeper.consume(storeName, T::class)
    }

    override fun onCloseScope() {
        storeInstanceKeeper.removeStore(key = storeName)
    }

    protected fun closeFragment() {
        navigationStoreSubject.onNext(NavigationIntent.OnBackPressed)
    }
}