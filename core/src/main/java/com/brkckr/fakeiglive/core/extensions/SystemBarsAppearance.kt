package com.brkckr.fakeiglive.core.extensions

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SystemBarsAppearance(useDarkIcons: Boolean) {
    val view = LocalView.current
    if (view.isInEditMode) return

    DisposableEffect(view, useDarkIcons) {
        val window = (view.context as? Activity)?.window
        if (window != null) {
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = useDarkIcons
                isAppearanceLightNavigationBars = useDarkIcons
            }
        }
        onDispose { }
    }
}
