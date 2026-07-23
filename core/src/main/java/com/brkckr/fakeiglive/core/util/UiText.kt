package com.brkckr.fakeiglive.core.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

// sealed class to handle both dynamic and resource-based strings
sealed class UiText {
    data class DynamicString(val value: String) : UiText() // for raw strings
    class StringResource(@StringRes val resId: Int, vararg val args: Any) : UiText() // for resource ids

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }
    }
}
