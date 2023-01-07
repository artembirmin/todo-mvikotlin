/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.common.mvibase

sealed class CommonLabel {
    object ShowLoading : CommonLabel()
    object HideLoading : CommonLabel()
    data class ShowError(val error: Throwable) : CommonLabel()
}
