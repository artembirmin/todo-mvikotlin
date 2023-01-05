/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.presentation.base.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * [RecyclerView.ViewHolder] implementation using DataBinding.
 * [B] is type of DataBinding generated class for your [DataBindingViewItem] layout.
 * [binding] is instance of [B].
 */
class DataBindingViewHolder<B : ViewDataBinding>(val binding: B) :
    RecyclerView.ViewHolder(binding.root)
