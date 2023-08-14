package com.abhilash.apps.animecatalogue.view.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import kotlinx.coroutines.delay


@OptIn(ExperimentalFoundationApi::class)
fun Modifier.pagerTabIndicatorOffset(
    pagerState: PagerState,
    tabPositions: List<TabPosition>,
    pageIndexMapping: (Int) -> Int = { it },
): Modifier = layout { measurable, constraints ->
    if (tabPositions.isEmpty()) {
        // If there are no pages, nothing to show
        layout(constraints.maxWidth, 0) {}
    } else {
        val currentPage = minOf(tabPositions.lastIndex, pageIndexMapping(pagerState.currentPage))
        val currentTab = tabPositions[currentPage]
        val previousTab = tabPositions.getOrNull(currentPage - 1)
        val nextTab = tabPositions.getOrNull(currentPage + 1)
        val fraction = pagerState.currentPageOffsetFraction
        val indicatorWidth = if (fraction > 0 && nextTab != null) {
            lerp(currentTab.width, nextTab.width, fraction).roundToPx()
        } else if (fraction < 0 && previousTab != null) {
            lerp(currentTab.width, previousTab.width, -fraction).roundToPx()
        } else {
            currentTab.width.roundToPx()
        }
        val indicatorOffset = if (fraction > 0 && nextTab != null) {
            lerp(currentTab.left, nextTab.left, fraction).roundToPx()
        } else if (fraction < 0 && previousTab != null) {
            lerp(currentTab.left, previousTab.left, -fraction).roundToPx()
        } else {
            currentTab.left.roundToPx()
        }
        val placeable = measurable.measure(
            Constraints(
                minWidth = indicatorWidth,
                maxWidth = indicatorWidth,
                minHeight = 0,
                maxHeight = constraints.maxHeight
            )
        )
        layout(constraints.maxWidth, maxOf(placeable.height, constraints.minHeight)) {
            placeable.placeRelative(
                indicatorOffset,
                maxOf(constraints.minHeight - placeable.height, 0)
            )
        }
    }
}


fun Modifier.drawBottomShade(): Modifier = run {
    drawBehind {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Black.copy(alpha = 0f),
                    Color.Black.copy(alpha = 0f),
                    Color.Black.copy(alpha = 0f),
                    Color.Black.copy(alpha = 0f),
                    Color.Black.copy(alpha = 0f),
                    Color.Black.copy(alpha = 0f),
                    Color.Black.copy(alpha = 0f),
                    Color.Black.copy(alpha = 0f),
                    Color.Black.copy(alpha = 0f),
                    Color.Black.copy(alpha = 0f),
                    Color.Black.copy(alpha = 0f),
                    Color.Black.copy(alpha = 0f),
                    Color.Black.copy(alpha = 0f),
                    Color.Black.copy(alpha = 0f),
                    Color.Black.copy(alpha = 0.1f),
                    Color.Black.copy(alpha = 0.1f),
                    Color.Black.copy(alpha = 0.1f),
                    Color.Black.copy(alpha = 0.1f),
                    Color.Black.copy(alpha = 0.15f),
                    Color.Black.copy(alpha = 0.20f),
                    Color.Black.copy(alpha = 0.25f),
                    Color.Black.copy(alpha = 0.3f),
                    Color.Black.copy(alpha = 0.5f),
                    Color.Black.copy(alpha = 0.5f),
                ),
            ),
            topLeft = Offset.Zero,
            size = size
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoScrollHelper(
    pagerState: PagerState,
    size: Int
) {
    LaunchedEffect(key1 = pagerState) {
        while (true) {
            delay(3000)
            pagerState.animateScrollToPage(
                (pagerState.currentPage+1) % size
            )
        }
    }
}

@Composable
fun Separator() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.Gray)
    )
}
