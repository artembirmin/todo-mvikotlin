/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.entity.errors

data class AppError(
    val error: Throwable,
    val payload: Any? = null
)