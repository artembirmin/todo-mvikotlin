/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.utils.ext

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun Int?.minToSeconds(): Int? {
    return this?.let { it * 60 }
}

private val formatSymbols = DecimalFormatSymbols().apply {
    decimalSeparator = '.'
    groupingSeparator = ' '
}

private const val defaultNumberPatter = "###,###.##"

fun Int.formatNumber(
    pattern: String = defaultNumberPatter,
    symbols: DecimalFormatSymbols = formatSymbols
): String {
    val formatter = DecimalFormat(pattern, symbols)
    return formatter.format(this)
}

fun Double.formatNumber(
    pattern: String = defaultNumberPatter,
    symbols: DecimalFormatSymbols = formatSymbols
): String {
    val formatter = DecimalFormat(pattern, symbols)
    return formatter.format(this)
}

fun Float.formatNumber(
    pattern: String = defaultNumberPatter,
    symbols: DecimalFormatSymbols = formatSymbols
): String {
    val formatter = DecimalFormat(pattern, symbols)
    return formatter.format(this)
}

fun Boolean.toInt() = if (this) 1 else 0
fun Boolean?.toInt() = this?.let { if (it) 1 else 0 }