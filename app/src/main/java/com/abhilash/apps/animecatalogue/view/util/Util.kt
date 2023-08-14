package com.abhilash.apps.animecatalogue.view.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import com.abhilash.apps.animecatalogue.model.AnimeArticle

@Composable
fun TextUnit.toPx(): Float {
    with(LocalDensity.current) {
        return this@toPx.toPx()
    }
}


val AnimeArticle.timeLine: String
    get() {
        val startYear = startDate.split("-").first()
        val endYear = endDate?.split("-")?.first() ?: "Current"

        return "$startYear - $endYear"
    }
