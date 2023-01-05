/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.utils.ext

fun <R> Boolean.ifTrue(block: (Boolean) -> R): Boolean {
    if (this) run(block)
    return this
}

fun <R> Boolean.ifFalse(block: (Boolean) -> R): Boolean {
    if (!this) run(block)
    return this
}

fun String.removeWhitespaces() = filter { !it.isWhitespace() }