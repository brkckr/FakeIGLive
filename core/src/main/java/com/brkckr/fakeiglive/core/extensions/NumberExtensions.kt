package com.brkckr.fakeiglive.core.extensions

import java.util.Locale

fun Int.toCompactViewerCount(): String = when {
    this < 1_000 -> toString()
    this < 100_000 -> String.format(Locale.US, "%.1fK", this / 1_000f)
    this < 1_000_000 -> "${this / 1_000}K"
    else -> String.format(Locale.US, "%.1fM", this / 1_000_000f)
}
