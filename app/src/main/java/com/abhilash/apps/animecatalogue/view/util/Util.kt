package com.abhilash.apps.animecatalogue.view.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.TextUnit
import com.abhilash.apps.animecatalogue.model.AnimeArticle
import java.util.Locale

@Composable
fun TextUnit.toPx(): Float {
    with(LocalDensity.current) {
        return this@toPx.toPx()
    }
}


val AnimeArticle.timeLine: String
    get() {
        val startYear = startDate.split("-").first()
        val endYear = endDate?.split("-")?.first() ?: "Present"

        return "$startYear - $endYear"
    }

fun Int.takeIfNonZero() = takeIf { it != 0 }

fun String.capitalizeFirstLetter() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
