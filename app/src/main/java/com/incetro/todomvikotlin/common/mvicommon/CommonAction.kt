/*
 * todomvikotlin
 *
 * Created by artembirmin on 28/1/2023.
 */

package com.incetro.todomvikotlin.common.mvicommon

import com.arkivanov.mvikotlin.core.utils.JvmSerializable

sealed class CommonAction : JvmSerializable {
    object Init : CommonAction()
}