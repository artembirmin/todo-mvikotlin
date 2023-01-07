/*
 * todomvikotlin
 *
 * Created by artembirmin on 6/1/2023.
 */

package com.incetro.todomvikotlin.common.mvibase

import androidx.lifecycle.LifecycleOwner

interface LifecycledConsumer<in T> : LifecycleOwner {

    val input: (T) -> Unit
}
