/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.presentation.base.adapter

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Displayed item in [RecyclerView].
 * Used together with [DataBindingDiffAdapter].
 * [B] is type of DataBinding generated class for your [DataBindingViewItem] layout.
 */
interface DataBindingViewItem<B : ViewDataBinding> {

    @LayoutRes
    fun getLayoutId(): Int

    /**
     * Binds item to [holder].
     */
    fun bind(holder: DataBindingViewHolder<B>)

    fun onPositionChanged(position: Int, size: Int) {}
}
