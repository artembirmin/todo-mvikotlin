/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.presentation.base.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.arkivanov.essenty.instancekeeper.instanceKeeper
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.store.Store
import com.incetro.todomvikotlin.app.AppActivity
import com.incetro.todomvikotlin.common.di.componentmanager.ComponentManager
import com.incetro.todomvikotlin.common.di.componentmanager.ComponentsStore
import com.incetro.todomvikotlin.common.mvibase.CommonLabel
import com.incetro.todomvikotlin.common.mvibase.NavigationLabel
import com.incetro.todomvikotlin.common.mvirxjava.bind
import com.incetro.todomvikotlin.common.mvirxjava.labels
import com.incetro.todomvikotlin.common.navigation.AppRouter
import com.incetro.todomvikotlin.entity.errors.AppError
import com.incetro.todomvikotlin.presentation.LoadingIndicator
import com.incetro.todomvikotlin.presentation.base.BaseView
import com.incetro.todomvikotlin.presentation.base.messageshowing.ErrorHandler
import moxy.MvpAppCompatFragment
import javax.inject.Inject

/**
 * Contains basic functionality for all [Fragment]s.
 */
abstract class BaseFragment<Binding : ViewDataBinding> : MvpAppCompatFragment(), BaseView {

    /**
     * Instance of [ViewDataBinding] class implementation for fragment.
     */
    protected lateinit var binding: Binding

    @Inject
    lateinit var errorHandler: ErrorHandler

    @Inject
    lateinit var router: AppRouter

    /** Layout id from res/layout. */
    abstract val layoutRes: Int

    /**
     * True, when [onSaveInstanceState] called.
     */
    private var isInstanceStateSaved: Boolean = false

    private val loadingIndicator: LoadingIndicator by lazy { LoadingIndicator(requireActivity()) }

    abstract val store: Store<*, *, CommonLabel>

    protected val storeInstanceKeeper by lazy { instanceKeeper() }

    /**
     * Does dependency injection.
     * Use [ComponentManager] implementation in dagger component and call [ComponentManager.getComponent].
     * Ex. SomeScreenComponent.ComponentManager.getComponent().inject(this)
     */
    abstract fun inject()

    /**
     * Removes corresponding dagger component from [ComponentsStore].
     * Use [ComponentManager] implementation in dagger component and call [ComponentManager.releaseComponent].
     * Ex. SomeScreenComponent.ComponentManager.releaseComponent()
     */
    abstract fun release()

    /**
     * Called in [AppActivity.onBackPressed].
     */
    open fun onBackPressed() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lifecycle = viewLifecycleOwner.essentyLifecycle()
        bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
            store.labels bindTo ::handleCommonLabel
        }
        lifecycle.doOnCreate { store.init() }
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        isInstanceStateSaved = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        isInstanceStateSaved = true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (needCloseScope()) {
            release()
        }
    }

    /**
     * Checks if the component needs to be released.
     */
    private fun needCloseScope(): Boolean =
        when {
            activity?.isChangingConfigurations == true -> false
            activity?.isFinishing == true -> true
            else -> isRealRemoving()
        }

    /**
     * `True` if current fragment removing now.
     */
    fun isRealRemoving(): Boolean =
        (isRemoving && !isInstanceStateSaved) //because isRemoving == true for fragment in backstack on screen rotation
                || ((parentFragment as? BaseFragment<*>)?.isRealRemoving() ?: false)

    override fun showError(error: Throwable) {
        showError(AppError(error))
    }

    override fun showError(error: AppError) {
        hideProgress()
        errorHandler.showError(error, requireContext())
    }

    override fun showMessageByAlertDialog(
        @StringRes title: Int?,
        @StringRes message: Int?,
        @StringRes positiveText: Int,
        @StringRes negativeText: Int?,
        onPositiveButtonClick: (() -> Unit)?,
        onNegativeButtonClick: (() -> Unit)?,
        onDismiss: (() -> Unit)?
    ) {
        AlertDialog.Builder(requireContext())
            .setMessage(message?.let { requireContext().getString(it) })
            .apply {
                negativeText?.let { setNegativeButton(it) { _, _ -> onNegativeButtonClick?.invoke() } }
                title?.let { setTitle(requireContext().getString(it)) }
            }
            .setPositiveButton(positiveText) { _, _ -> onPositiveButtonClick?.invoke() }
            .setOnDismissListener { onDismiss?.invoke() }
            .create()
            .show()
    }

    override fun showMessageByToast(message: Int, duration: Int) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    override fun showMessageByToast(message: String, duration: Int) {
        Toast.makeText(requireContext(), message, duration).show()
    }


    fun showProgress() {
        loadingIndicator.show()
    }

    fun hideProgress() {
        loadingIndicator.dismiss()
    }

    fun hideProgressForce() {
        loadingIndicator.forceDismiss()
    }
}