/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.demouserstory.demoscreen

import android.os.Bundle
import android.view.View
import com.incetro.todomvikotlin.R
import com.incetro.todomvikotlin.databinding.FragmentDemoBinding
import com.incetro.todomvikotlin.presentation.base.fragment.BaseFragment
import com.incetro.todomvikotlin.presentation.userstory.demouserstory.di.DemoComponent
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class DemoFragment : BaseFragment<FragmentDemoBinding>(), DemoView {

    override val layoutRes = R.layout.fragment_demo

    @Inject
    @InjectPresenter
    lateinit var presenter: DemoPresenter

    @ProvidePresenter
    fun providePresenter(): DemoPresenter = presenter

    override fun inject() = DemoComponent.Manager.getComponent().inject(this)
    override fun release() = DemoComponent.Manager.releaseComponent()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(binding) {

        }
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    companion object {
        fun newInstance() = DemoFragment()
    }
}