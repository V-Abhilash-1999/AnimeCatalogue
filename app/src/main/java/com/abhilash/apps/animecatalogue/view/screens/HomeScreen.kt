@file:OptIn(ExperimentalFoundationApi::class)

package com.abhilash.apps.animecatalogue.view.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.abhilash.apps.animecatalogue.Screen
import com.abhilash.apps.animecatalogue.model.AnimeData
import com.abhilash.apps.animecatalogue.view.theme.LocalNavigateToLambda
import com.abhilash.apps.animecatalogue.view.util.AnimePoster
import com.abhilash.apps.animecatalogue.view.util.LoadingScreen
import com.abhilash.apps.animecatalogue.view.util.RatingText
import com.abhilash.apps.animecatalogue.view.util.UIContentType
import com.abhilash.apps.animecatalogue.view.util.UIState
import com.abhilash.apps.animecatalogue.view.util.VerticalSpacer
import com.abhilash.apps.animecatalogue.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val homeViewModel: HomeViewModel = viewModel()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Anime Catalogue")
                },
                navigationIcon = {

                }
            )
        }
    ) {
        val uiState by homeViewModel.uiState

        CatalogueScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            uiState = uiState
        )
    }
}

@Composable
private fun CatalogueScreen(
    modifier: Modifier,
    uiState: UIState
) {
    Box(modifier = modifier) {
        when (uiState) {
            UIState.SUCCESS -> {
                Catalogue()
            }

            UIState.FAILURE -> {

            }

            UIState.LOADING -> {
                LoadingScreen()
            }

            UIState.EMPTY_DATA -> {

            }
        }
    }
}

@Composable
private fun Catalogue() {
    val segmentClass = remember {
        mutableListOf(
            HomeSegment.Popular(),
            HomeSegment.Action(),
            HomeSegment.Adventure(),
            HomeSegment.Romance(),
            HomeSegment.Isekai(),
            HomeSegment.SliceOfLife(),
        )
    }
    val homeViewModel = viewModel<HomeViewModel>()
    LaunchedEffect(key1 = Unit) {
        launch(Dispatchers.IO) {
            segmentClass.forEach { segment ->
                homeViewModel.setCategoryData(segment)
            }
        }
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        segmentClass.forEach { homeSegment ->
            item(
                contentType = UIContentType.SEGMENT_HEADER.name,
                key = "${homeSegment.title}_${UIContentType.SEGMENT_HEADER.name}",
            ) {
                Text(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                    text = homeSegment.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            item(
                contentType = UIContentType.LIST_ITEM.name,
                key = "${homeSegment.title}_${UIContentType.LIST_ITEM.name}"
            ) {
                AnimeSegmentList(homeSegment)
            }
        }

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AnimeSegmentList(
    segmentData: HomeSegment
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 200.dp)
            .animateContentSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        if(segmentData.dataList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .height(200.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(36.dp),
                        color = Color.Green,
                        strokeWidth = 2.dp
                    )
                }
            }
        }
        items(
            items = segmentData.dataList,
            key = { it.id }
        ) { animeData ->
            val navigateTo = LocalNavigateToLambda.current
            Column(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = rememberRipple(
                            radius = 150.dp,
                            bounded = false
                        )
                    ) {
                        navigateTo(Screen.DETAIL.name, animeData.id)
                    }
                    .padding(8.dp)
                    .animateItemPlacement()
                    .width(IntrinsicSize.Min)
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
    }
}

@Composable
private fun AnimeTitle(
    text: String
) {
    var adjustedText by remember(text) {
        mutableStateOf(text)
    }
    Text(
        modifier = Modifier.fillMaxWidth(),
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

@Stable
sealed class HomeSegment(
    val title: String,
    val category: String,
    val dataList: SnapshotStateList<AnimeData>
) {
    class Popular: HomeSegment("Popular Anime", "Popular", mutableStateListOf())
    class Action: HomeSegment("Action Anime", "Action", mutableStateListOf())
    class Adventure: HomeSegment("Adventure Anime", "Adventure", mutableStateListOf())
    class Romance: HomeSegment("Romance Anime", "Romance", mutableStateListOf())
    class Isekai: HomeSegment("Isekai Anime", "Isekai", mutableStateListOf())
    class SliceOfLife: HomeSegment("Slice of Life Anime", "Slice of Life", mutableStateListOf())
}