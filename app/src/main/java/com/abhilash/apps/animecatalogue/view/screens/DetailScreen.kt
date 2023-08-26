package com.abhilash.apps.animecatalogue.view.screens

import android.nfc.Tag
import android.util.EventLogTags.Description
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorLong
import androidx.lifecycle.viewmodel.compose.viewModel
import com.abhilash.apps.animecatalogue.R
import com.abhilash.apps.animecatalogue.model.AnimeArticle
import com.abhilash.apps.animecatalogue.model.CategoryAttribute
import com.abhilash.apps.animecatalogue.view.util.AnimePoster
import com.abhilash.apps.animecatalogue.view.util.HorizontalSpacer
import com.abhilash.apps.animecatalogue.view.util.LoadingScreen
import com.abhilash.apps.animecatalogue.view.util.RatingText
import com.abhilash.apps.animecatalogue.view.util.VerticalSpacer
import com.abhilash.apps.animecatalogue.view.util.capitalizeFirstLetter
import com.abhilash.apps.animecatalogue.view.util.takeIfNonZero
import com.abhilash.apps.animecatalogue.viewmodel.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.AttributedCharacterIterator.Attribute
import java.util.Locale

@Composable
fun AnimeDetailScreen(
    animeId: String
) {
    val detailViewModel = viewModel<DetailViewModel>()

    val animeArticle by detailViewModel.animeArticle
    val animeCategoryAttributeList = detailViewModel.categoryAttributeList

    LaunchedEffect(key1 = Unit) {
        launch(Dispatchers.IO) {
            detailViewModel.getAnimeData(animeId)
        }
    }

    when(animeArticle) {
        null -> {
            LoadingScreen()
        }

        else -> {
            animeArticle?.let {
                AnimeDetail(
                    animeArticle = it,
                    categoryAttribute = animeCategoryAttributeList
                )
            }
        }
    }
}

@Composable
private fun AnimeDetail(
    animeArticle: AnimeArticle,
    categoryAttribute: SnapshotStateList<CategoryAttribute>
) {
    BackdropImageScaffold(
        modifier = Modifier
            .fillMaxSize(),
        backgroundImageUrl = animeArticle.posterImage.largeImageUrl,
        contentPaddingValues = PaddingValues(top = 24.dp, start = 16.dp, end = 16.dp),
        contentBackgroundColor = Color.White
    ) {
        TitleRow(
            title = animeArticle.canonicalTitle,
            rating = animeArticle.averageRating
        )

        VerticalSpacer(16.dp)

        AnimeTags(tags = categoryAttribute.map { it.title })

        VerticalSpacer(16.dp)

        Attribute(
            length = animeArticle.episodeLength.takeIfNonZero() ?: animeArticle.totalLength,
            rating = animeArticle.ageRating,
            status = animeArticle.status
        )

        VerticalSpacer(24.dp)

        Description(
            description = animeArticle.description
        )

    }
}

@Composable
private fun Description(
    description: String
) {
    Text(
        text = "Description",
        fontSize = 16.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.Black
    )

    VerticalSpacer(8.dp)

    Text(
        text = description,
        fontSize = 14.sp,
        fontWeight = FontWeight.Light,
        color = Color.Gray
    )
}

@Composable
private fun Attribute(
    length: Int,
    rating: String,
    status: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        length.takeIfNonZero()?.let {
            AttributeItem(
                topText = "Avg. Length",
                bottomText = length.toString()
            )
        }

        AttributeItem(
            topText = "Rating",
            bottomText = rating
        )

        AttributeItem(
            topText = "Status",
            bottomText = status.capitalizeFirstLetter()
        )

        HorizontalSpacer(width = 32.dp)
    }
}

@Composable
private fun AttributeItem(
    topText: String,
    bottomText: String
) {
    Column {
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
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AnimeTags(
    tags: List<String?>
) {
    FlowRow(
        modifier = Modifier
            .animateContentSize()
    ) {
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
                rating = "${String.format(" % .2f", rating / 10)}/10"
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
                .fillMaxHeight(0.4f),
            url = backgroundImageUrl,
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = (this@BoxWithConstraints.constraints.maxHeight * .6f).dp)
                    .offset(y = (this@BoxWithConstraints.maxHeight.value * (0.4)).dp - 20.dp)
                    .background(
                        color = contentBackgroundColor,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(contentPaddingValues)
            ) {
                content()
            }
        }
    }
}