package com.abhilash.apps.animecatalogue.view.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.TextUnit
import java.util.Locale

@Composable
fun TextUnit.toPx(): Float {
    with(LocalDensity.current) {
        return this@toPx.toPx()
    }
}

fun Int.takeIfNonZero() = takeIf { it != 0 }

fun String.capitalizeFirstLetter() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

enum class LoaderUrls(val url: String) {
    PIKACHU("https://media.tenor.com/fSsxftCb8w0AAAAi/pikachu-running.gif"),
    SHARINGAN("https://media.tenor.com/VNmsolVRhQcAAAAi/sharingan.gif")
}