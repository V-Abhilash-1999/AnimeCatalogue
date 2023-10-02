package com.abhilash.apps.animecatalogue.view.screens

import android.content.Intent
import android.net.Uri
import android.webkit.JavascriptInterface
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhilash.apps.animecatalogue.R
import com.abhilash.apps.animecatalogue.model.AnimeData
import com.abhilash.apps.animecatalogue.view.util.AnimePoster
import com.abhilash.apps.animecatalogue.view.util.HorizontalSpacer
import com.abhilash.apps.animecatalogue.view.util.LoaderUrls
import com.abhilash.apps.animecatalogue.view.util.AnimeLoader
import com.abhilash.apps.animecatalogue.view.util.ComingSoon
import com.abhilash.apps.animecatalogue.view.util.RatingText
import com.abhilash.apps.animecatalogue.view.util.VerticalSpacer
import com.abhilash.apps.animecatalogue.view.util.capitalizeFirstLetter
import com.abhilash.apps.animecatalogue.view.util.drawBottomShade
import com.abhilash.apps.animecatalogue.view.util.takeIfNonZero
import com.abhilash.apps.animecatalogue.viewmodel.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AnimeDetailScreen(
    animeId: String
) {
    val detailViewModel = hiltViewModel<DetailViewModel>()

    val animeArticle by detailViewModel.animeData
    val animeCategoryAttributeList = detailViewModel.categoryAttributeList

    LaunchedEffect(key1 = Unit) {
        launch(Dispatchers.IO) {
            detailViewModel.getAnimeData(animeId)
        }
    }

    when(animeArticle) {
        null -> {
            AnimeLoader(
                modifier = Modifier.fillMaxSize(),
                loaderGifUrl = LoaderUrls.PIKACHU.url
            )
        }

        else -> {
            animeArticle?.let {
                AnimeDetail(
                    animeData = it,
                    categoryAttribute = animeCategoryAttributeList
                )
            }
        }
    }
}

@Composable
private fun AnimeDetail(
    animeData: AnimeData,
    categoryAttribute: SnapshotStateList<String>
) {
    BackdropImageScaffold(
        modifier = Modifier
            .fillMaxSize(),
        backgroundImageUrl = animeData.images.jpg.largeImageUrl,
        contentPaddingValues = PaddingValues(top = 24.dp, start = 16.dp, end = 16.dp),
        contentBackgroundColor = Color.White
    ) {
        TitleRow(
            title = animeData.title,
            rating = animeData.score
        )

        VerticalSpacer(16.dp)

        AnimeTags(
            tags = categoryAttribute
        )

        VerticalSpacer(16.dp)

        Attribute(
            length = animeData.episodes,
            rating = animeData.rating,
            status = animeData.runningStatus
        )

        VerticalSpacer(24.dp)

        Description(
            description = animeData.description
        )

        Trailer(
            videoId = animeData.trailer.videoId,
            thumbnailUrl = animeData.trailer.images.largeImageUrl
        )

        RelatedAnime()
    }
}

@Composable
fun RelatedAnime() {
    ContentWithHeader(header = "Related Anime") {
        ComingSoon(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}

@Composable
private fun Trailer(
    videoId: String?,
    thumbnailUrl: String
) {
    videoId ?: return
    ContentWithHeader(header = "Trailer") {
        TrailerPreview(
            videoId = videoId,
            thumbnailUrl = thumbnailUrl
        )
    }
}

@Composable
private fun TrailerPreview(
    videoId: String,
    thumbnailUrl: String
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
                intent.putExtra("VIDEO_ID", videoId)
                startActivity(context, intent, bundleOf())
            }
    ) {
        AnimePoster(
            modifier = Modifier
                .fillMaxSize(),
            url = thumbnailUrl
        )

        Icon(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center)
                .background(Color.White, RoundedCornerShape(100)),
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = "Play"
        )
    }
}

@Composable
private fun RelatedAnimeList() {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(0) {

        }
    }
}

@Composable
private fun Description(
    description: String
) {
    ContentWithHeader(header = "Description") {
        Text(
            text = description,
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray
        )
    }
}

@Composable
private fun ContentWithHeader(
    header: String,
    content: @Composable () -> Unit
) {
    HeaderText(text = header)

    VerticalSpacer(8.dp)

    content()

    VerticalSpacer(32.dp)
}

@Composable
private fun HeaderText(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.Black
    )
}

@Composable
private fun Attribute(
    length: Int,
    rating: String,
    status: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        length.takeIfNonZero()?.let {
            AttributeItem(
                modifier = Modifier.weight(2f),
                topText = "Avg. Length",
                bottomText = length.toString()
            )

            HorizontalSpacer(width = 32.dp)
        }

        AttributeItem(
            modifier = Modifier.weight(2f),
            topText = "Rating",
            bottomText = rating
        )

        HorizontalSpacer(width = 32.dp)

        AttributeItem(
            modifier = Modifier.weight(2f),
            topText = "Status",
            bottomText = status.capitalizeFirstLetter()
        )
    }
}

@Composable
private fun AttributeItem(
    modifier: Modifier = Modifier,
    topText: String,
    bottomText: String
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = topText,
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Light
        )

        VerticalSpacer(4.dp)

        Text(
            text = bottomText,
            fontSize = 14.sp,
            color = Color.Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AnimeTags(
    tags: List<String?>
) {
    FlowRow {
        tags.forEach {
            key(it) {
                Tag(text = it.toString())
            }
        }
    }
}

class TagPreview: PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = sequenceOf("Action", "Adventure", "Romance")

}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
private fun Tag(
    @PreviewParameter(TagPreview::class) text: String
) {
    Text(
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(50))
            .padding(vertical = 4.dp, horizontal = 8.dp),
        text = text,
        fontSize = 12.sp,
    )
}

@Preview(
    name = "Title Row Sample",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun TitleRow(
    title: String = "One Piece",
    rating: Float = 86.23f
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )

            RatingText(
                modifier = Modifier,
                rating = "${String.format(" % .2f", rating)}/10"
            )
        }

        Icon(
            modifier = Modifier
                .padding(12.dp)
                .size(24.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = R.drawable.ic_bookmark),
            contentDescription = null
        )
    }
}


@Composable
private fun BackdropImageScaffold(
    modifier: Modifier,
    backgroundImageUrl: String,
    contentBackgroundColor: Color,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        AnimePoster(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .drawBottomShade(),
            url = backgroundImageUrl,
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .heightIn(min = (this@BoxWithConstraints.constraints.maxHeight * .5f).dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .offset(y = (this@BoxWithConstraints.maxHeight.value * (0.4)).dp - 20.dp)
                    .background(
                        color = contentBackgroundColor,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(contentPaddingValues)
            ) {
                content()

                VerticalSpacer((this@BoxWithConstraints.maxHeight.value * (0.4)).dp - 20.dp)
            }
        }
    }
}


internal class JSInterface {
    @JavascriptInterface
    fun doSomething() {

    }
}