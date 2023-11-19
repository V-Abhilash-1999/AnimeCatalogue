package com.abhilash.apps.animecatalogue.view.util

import android.os.Build
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.abhilash.apps.animecatalogue.model.datastore.LoginMode
import com.abhilash.apps.animecatalogue.view.screens.AnimeScreen
import com.abhilash.apps.animecatalogue.view.state.AuthTokenState


fun Modifier.drawBottomShade(
    color: Color = Color.Black
): Modifier = run {
    drawBehind {
        drawRect(
            brush = Brush.verticalGradient(
                colorStops = arrayOf(
                    0f to color.copy(0f),
                    0.4f to color.copy(0f),
                    0.5f to color.copy(0.2f),
                    1f to color.copy(0.5f)
                ),
            ),
            topLeft = Offset.Zero,
            size = size
        )
    }
}


fun Modifier.drawTopShade(
    color: Color = Color.Black
): Modifier = run {
    drawBehind {
        drawRect(
            brush = Brush.verticalGradient(
                colorStops = arrayOf(
                    0f to color.copy(0.5f),
                    0.4f to color.copy(0.2f),
                    0.5f to color.copy(0f),
                    1f to color.copy(0f)
                ),
            ),
            topLeft = Offset.Zero,
            size = size
        )
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
fun AnimeLoader(
    modifier: Modifier,
    loaderGifUrl: String
) {
    Box(
        modifier = modifier
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
            model = loaderGifUrl,
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

@Composable
fun ComingSoon(
    modifier: Modifier
) {
    AnimePoster(
        modifier = modifier,
        url = "https://media.tenor.com/UmO6MCW_KwwAAAAC/luffy-one-piece.gif"
    )
}

@Composable
fun rememberStartScreen(
    loginMode: LoginMode,
    authTokenState: AuthTokenState
): AnimeScreen {
    return remember(loginMode, authTokenState) {
        when(loginMode) {
            LoginMode.GUEST -> {
                AnimeScreen.HOME
            }
            LoginMode.USER_LOGIN -> {
                if(authTokenState is AuthTokenState.Success) AnimeScreen.HOME else AnimeScreen.LOGIN
            }
            else -> {
                AnimeScreen.LOGIN
            }
        }
    }
}

@Composable
fun SplashScreen() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "To Insert Logo",
            )
        }
    }
}

fun Modifier.shimmerBackground(): Modifier = composed {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition(label = "Genre Shimmer")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(800), repeatMode = RepeatMode.Reverse
        ),
        label = "Genre Shimmer Translation"
    )
    val brush =  Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )
    background(brush)
}
