package com.abhilash.apps.animecatalogue.view.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhilash.apps.animecatalogue.model.AnimeData
import com.abhilash.apps.animecatalogue.view.util.AnimePoster
import com.abhilash.apps.animecatalogue.view.util.RatingText
import com.abhilash.apps.animecatalogue.view.util.VerticalSpacer
import com.abhilash.apps.animecatalogue.view.util.shimmerBackground


@Composable
fun AnimeCard(
    modifier: Modifier,
    animeData: AnimeData
) {
    Column(
        modifier = modifier
    ) {
        AnimePoster(
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = false
                )
                .height(200.dp)
                .aspectRatio(0.7f)
                .clip(RoundedCornerShape(8.dp)),
            url = animeData.images.webp.imageUrl,
            contentScale = ContentScale.FillBounds
        )

        VerticalSpacer(16.dp)

        AnimeTitle(text = animeData.englishTitle ?: animeData.title)

        VerticalSpacer()

        RatingText(
            rating = String.format("%.2f", animeData.score)
        )
    }
}

@Composable
fun AnimeTitle(
    modifier: Modifier = Modifier,
    text: String
) {
    var adjustedText by remember(text) {
        mutableStateOf(text)
    }
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        text = adjustedText,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 14.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = {
            if(it.lineCount == 1) {
                adjustedText += "\n"
            }
        }
    )
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.ShimmeringAnimeCard() {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .animateItemPlacement()
            .width(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(0.7f)
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerBackground(),
        )

        VerticalSpacer(16.dp)

        AnimeTitle(
            modifier = Modifier
                .shimmerBackground(),
            text = ""
        )

        VerticalSpacer()

        RatingText(
            modifier = Modifier.shimmerBackground(),
            rating = "  "
        )
    }
}