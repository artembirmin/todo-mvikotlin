/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.common.mvicommon

import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.github.terrakok.cicerone.Screen

sealed class CommonLabel : JvmSerializable {
    object ShowLoading : CommonLabel()
    object HideLoading : CommonLabel()
    data class ShowError(val error: Throwable) : CommonLabel()
    data class ShowMessageByToast(val message: String) : CommonLabel()
}

sealed class NavigationLabel : CommonLabel() {
    data class NavigateTo(val screen: Screen) : NavigationLabel()
    data class NewRootScreen(val screen: Screen) : NavigationLabel()
    data class ReplaceScreen(val screen: Screen) : NavigationLabel()
    data class BackTo(val screen: Screen) : NavigationLabel()
    object Exit : NavigationLabel()
}