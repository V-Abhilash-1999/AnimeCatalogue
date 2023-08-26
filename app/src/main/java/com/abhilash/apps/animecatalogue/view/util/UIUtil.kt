package com.abhilash.apps.animecatalogue.view.util

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
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


fun Modifier.drawBottomShade(
    color: Color = Color.Black
): Modifier = run {
    drawBehind {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    color.copy(alpha = 0f),
                    color.copy(alpha = 0f),
                    color.copy(alpha = 0f),
                    color.copy(alpha = 0f),
                    color.copy(alpha = 0f),
                    color.copy(alpha = 0f),
                    color.copy(alpha = 0f),
                    color.copy(alpha = 0f),
                    color.copy(alpha = 0f),
                    color.copy(alpha = 0f),
                    color.copy(alpha = 0f),
                    color.copy(alpha = 0f),
                    color.copy(alpha = 0f),
                    color.copy(alpha = 0f),
                    color.copy(alpha = 0.1f),
                    color.copy(alpha = 0.1f),
                    color.copy(alpha = 0.1f),
                    color.copy(alpha = 0.1f),
                    color.copy(alpha = 0.15f),
                    color.copy(alpha = 0.20f),
                    color.copy(alpha = 0.25f),
                    color.copy(alpha = 0.3f),
                    color.copy(alpha = 0.5f),
                    color.copy(alpha = 0.5f),
                ),
            ),
            topLeft = Offset.Zero,
            size = size
        )
    }
}

fun Modifier.drawFullBottomShade(
    color: Color = Color.Black
): Modifier = run {
    drawBehind {
        drawRect(
            brush = Brush.verticalGradient(
                colorStops = arrayOf(
                    0f to Color.Transparent,
                    0.6f to Color.Transparent,
                    1f to color,
                )
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
            .background(Color.LightGray)
    )
}

@Composable
fun AnimePoster(
    modifier: Modifier = Modifier,
    url: String,
    contentScale: ContentScale = ContentScale.Crop
) {
    val imagePainter = rememberAsyncImagePainter(model = url)

    Image(
        modifier = modifier,
        painter = imagePainter,
        contentDescription = null,
        contentScale = contentScale
    )
}


@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val context = LocalContext.current
        val imageLoader = ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()

        val imagePainter = rememberAsyncImagePainter(
            model = "https://media.tenor.com/fSsxftCb8w0AAAAi/pikachu-running.gif",
            imageLoader = imageLoader
        )

        Image(
            modifier = Modifier
                .size(72.dp)
                .align(Alignment.Center),
            painter = imagePainter,
            contentDescription = null
        )
    }
}

@Composable
fun RatingText(
    modifier: Modifier = Modifier,
    rating: String
) {
    Row(
        modifier = modifier
    ) {
        Icon(
            painter = rememberVectorPainter(image = Icons.Default.Star),
            contentDescription = null,
            tint= Color(254, 176, 38, 255),
            modifier = Modifier
                .padding(end = 4.dp)
                .size(16.dp)
        )

        Text(
            text = rating,
            fontSize = 12.sp,
            lineHeight = 14.sp,
            color = Color(156,157,157,255)
        )
    }
}

@Composable
fun VerticalSpacer(height: Dp = 8.dp) {
    Spacer(Modifier.height(height = height))
}

@Composable
fun HorizontalSpacer(width: Dp = 8.dp) {
    Spacer(Modifier.width(width = width))
}