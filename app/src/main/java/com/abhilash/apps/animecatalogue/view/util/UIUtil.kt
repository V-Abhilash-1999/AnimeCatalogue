package com.abhilash.apps.animecatalogue.view.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit

@Composable
fun TextUnit.toPx(): Float {
    with(LocalDensity.current) {
        return this@toPx.toPx()
    }
}