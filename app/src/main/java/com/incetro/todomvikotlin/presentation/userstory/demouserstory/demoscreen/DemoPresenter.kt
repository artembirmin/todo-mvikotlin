/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.demouserstory.demoscreen

import com.incetro.todomvikotlin.common.navigation.AppRouter
import com.incetro.todomvikotlin.presentation.base.fragment.BasePresenter
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class DemoPresenter @Inject constructor(
    private val router: AppRouter,
) : BasePresenter<DemoView>() {

    override fun onFirstViewAttach() {

    }

    override fun onBackPressed() {
        router.exit()
    }
}