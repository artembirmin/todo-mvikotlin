/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.presentation.common.binding

import android.view.View
import androidx.databinding.BindingAdapter

object CommonBindingAdapters {

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun isViewVisible(view: View, isVisible: Boolean) {
        if (isVisible) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}