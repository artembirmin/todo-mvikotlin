/*
 * todomvikotlin
 *
 * Created by artembirmin on 10/1/2023.
 */

package com.incetro.todomvikotlin.presentation.userstory.task.taskinfo

import com.incetro.todomvikotlin.common.navigation.InitParams
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskInfoFragmentInitParams(val id: Int) : InitParams