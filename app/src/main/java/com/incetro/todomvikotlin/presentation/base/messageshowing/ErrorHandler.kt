/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.presentation.base.messageshowing

import android.app.AlertDialog
import android.content.Context
import com.incetro.todomvikotlin.R
import com.incetro.todomvikotlin.common.navigation.AppRouter
import com.incetro.todomvikotlin.entity.errors.AppError
import com.incetro.todomvikotlin.utils.ext.toReadableLogDateTime
import org.joda.time.DateTime
import timber.log.Timber
import javax.inject.Inject


class ErrorHandler @Inject constructor(
    private val appRouter: AppRouter
) {
    fun showError(appError: AppError, context: Context) {
        val error = appError.error
        showError(error, appError.payload, context)
    }

    fun showError(error: Throwable, context: Context) {
        showError(error, null, context)
    }

    private fun showError(error: Throwable, payload: Any? = null, context: Context) {
        Timber.e(error)
        showDebugError(error, context)
    }

    private fun showDebugError(
        error: Throwable,
        context: Context
    ) {
        val exceptionErrorMessage = error.message.toString()
        showDebugMessageByDialog(
            context,
            exceptionErrorMessage
        )
    }

    private fun showDebugMessageByDialog(
        context: Context,
        message: CharSequence = "",
        errorDate: String = DateTime.now().toReadableLogDateTime()
    ) {
        AlertDialog.Builder(context)
            .setTitle(errorDate)
            .setMessage(message)
            .setPositiveButton(R.string.alert_button_ok) { _, _ -> }
            .create().show()
    }
}

