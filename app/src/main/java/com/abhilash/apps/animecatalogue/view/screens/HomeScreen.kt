@file:OptIn(ExperimentalFoundationApi::class)

package com.abhilash.apps.animecatalogue.view.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.abhilash.apps.animecatalogue.Screen
import com.abhilash.apps.animecatalogue.model.AnimeArticle
import com.abhilash.apps.animecatalogue.model.AnimeData
import com.abhilash.apps.animecatalogue.view.theme.LocalNavigateToLambda
import com.abhilash.apps.animecatalogue.view.util.AnimePoster
import com.abhilash.apps.animecatalogue.view.util.AutoScrollHelper
import com.abhilash.apps.animecatalogue.view.util.LoadingScreen
import com.abhilash.apps.animecatalogue.view.util.RatingText
import com.abhilash.apps.animecatalogue.view.util.Separator
import com.abhilash.apps.animecatalogue.view.util.UIContentType
import com.abhilash.apps.animecatalogue.view.util.UIState
import com.abhilash.apps.animecatalogue.view.util.VerticalSpacer
import com.abhilash.apps.animecatalogue.view.util.drawBottomShade
import com.abhilash.apps.animecatalogue.view.util.pagerTabIndicatorOffset
import com.abhilash.apps.animecatalogue.view.util.timeLine
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
        item(
            contentType = UIContentType.CAROUSEL_VIEWPAGER.name,
            key = UIContentType.CAROUSEL_VIEWPAGER.name
        ) {
            AutoScrollingViewPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillParentMaxHeight(0.4f)
            )
        }

        item(
            contentType = UIContentType.SEPARATOR.name,
            key = UIContentType.SEPARATOR.name
        ) {
            Separator()

            VerticalSpacer(16.dp)
        }

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
        ) {
            val animeAttribute = it.attributes
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
                        navigateTo(Screen.DETAIL.name, it.id)
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
                    url = animeAttribute.posterImage.smallImageUrl,
                    contentScale = ContentScale.FillBounds
                )

                VerticalSpacer(16.dp)

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = animeAttribute.canonicalTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                VerticalSpacer()

                RatingText(
                    rating = String.format("%.2f", animeAttribute.averageRating / 10)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AutoScrollingViewPager(
    modifier: Modifier
) {
    val homeViewModel = viewModel<HomeViewModel>()

    val big3DataList = homeViewModel.big3StateList

    val pagerState = rememberPagerState()

    Column(modifier = modifier) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            state = pagerState,
            pageCount = big3DataList.size,
        ) { index ->
            AnimePage(big3DataList[index])
        }

        TabRow(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .align(Alignment.CenterHorizontally),
            selectedTabIndex = pagerState
                .currentPage,
            indicator = {
                Box(
                    modifier = Modifier
                        .pagerTabIndicatorOffset(
                            pagerState = pagerState,
                            tabPositions = it
                        )
                        .padding(4.dp)
                        .fillMaxHeight()
                        .width(24.dp)
                        .background(Color.DarkGray, RoundedCornerShape(100))
                )
            },
            divider = {}
        ) {
            big3DataList.forEach { _ ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .height(4.dp)
                        .width(24.dp)
                        .background(Color.LightGray, RoundedCornerShape(100))
                )
            }

        }

        VerticalSpacer(16.dp)
    }

    AutoScrollHelper(
        pagerState = pagerState,
        size = big3DataList.size
    )


}

@Composable
private fun AnimePage(article: AnimeArticle) {
    val animePageShape = RoundedCornerShape(12.dp)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = animePageShape
            )
            .clip(animePageShape)
    ) {
        AnimePoster(
            modifier = Modifier.fillMaxSize(),
            url = article.coverImage.originalImageUrl,
            contentScale = ContentScale.FillBounds
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBottomShade()
        )

        AnimeTitleBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            article = article
        )

    }
}

@Composable
private fun AnimeTitleBar(
    modifier: Modifier,
    article: AnimeArticle
) {
    Row(modifier = modifier) {
        AnimeTitleAndDuration(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            article = article
        )

        Row(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .background(Color(254, 176, 38, 255), RoundedCornerShape(50))
                .padding(4.dp)
                .align(Alignment.Bottom),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = rememberVectorPainter(image = Icons.Default.Star),
                contentDescription = null,
                tint= Color(121,87,21,255),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(16.dp)
            )

            Text(
                modifier = Modifier
                    .padding(end = 4.dp),
                text = String.format("%.1f", article.averageRating/10f),
                color = Color(121,87,21,255),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun AnimeTitleAndDuration(
    modifier: Modifier,
    article: AnimeArticle
) {
    Column(modifier = modifier) {
        Text(
            text = article.canonicalTitle,
            color = Color(255,254,254,255),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = article.timeLine,
            color = Color(255,255,255,210),
            fontSize = 14.sp
        )
    }
}


@Stable
sealed class HomeSegment(
    val title: String,
    val category: String,
    val dataList: SnapshotStateList<AnimeData>
) {
    class Popular: HomeSegment("Popular Anime", "*", mutableStateListOf())
    class Action: HomeSegment("Action Anime", "action", mutableStateListOf())
    class Adventure: HomeSegment("Adventure Anime", "adventure", mutableStateListOf())
    class Romance: HomeSegment("Romance Anime", "romance", mutableStateListOf())
    class Isekai: HomeSegment("Isekai Anime", "isekai", mutableStateListOf())
    class SliceOfLife: HomeSegment("Slice of Life Anime", "slice of life", mutableStateListOf())
}