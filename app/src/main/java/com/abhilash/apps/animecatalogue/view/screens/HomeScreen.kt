@file:OptIn(ExperimentalFoundationApi::class)

package com.abhilash.apps.animecatalogue.view.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.twotone.ExitToApp
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.abhilash.apps.animecatalogue.model.AnimeData
import com.abhilash.apps.animecatalogue.view.theme.LocalNavigateToLambda
import com.abhilash.apps.animecatalogue.view.util.AnimeLoader
import com.abhilash.apps.animecatalogue.view.util.AnimePoster
import com.abhilash.apps.animecatalogue.view.util.LoaderUrls
import com.abhilash.apps.animecatalogue.view.util.RatingText
import com.abhilash.apps.animecatalogue.view.util.UIContentType
import com.abhilash.apps.animecatalogue.view.util.UIState
import com.abhilash.apps.animecatalogue.view.util.VerticalSpacer
import com.abhilash.apps.animecatalogue.view.util.shimmerBackground
import com.abhilash.apps.animecatalogue.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val viewModel = hiltViewModel<HomeViewModel>()
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
        },
        bottomBar = {
            CatalogueBottomBar()
        }
    ) {
        val uiState by viewModel.uiState

        CatalogueScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            uiState = uiState
        )
    }
}

@Composable
private fun CatalogueBottomBar() {
    val navController = rememberNavController()
    val currentScreen = rememberSaveable {
        mutableStateOf(HomeAnimeScreen.ANIME_SERIES.name)
    }
    LaunchedEffect(currentScreen.value) {
        val route = navController.currentBackStackEntry?.destination?.route
        if(route != null && route != currentScreen.value) {
            navController.navigate(currentScreen.value)
        }
    }
    NavigationBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        HomeAnimeScreen.values().forEach {
            NavigationItem(
                isCurrentScreen = currentScreen.value == it.name,
                screen = it
            ) {
                currentScreen.value = it.name
            }
        }
    }
}

@Composable
private fun RowScope.NavigationItem(
    isCurrentScreen: Boolean,
    screen: HomeAnimeScreen,
    navigateToScreen: () -> Unit
) {
    NavigationBarItem(
        selected = isCurrentScreen,
        onClick = {
            navigateToScreen()
        },
        label = {
            val text = when(screen) {
                HomeAnimeScreen.ANIME_SERIES -> {
                    "Series"
                }
                HomeAnimeScreen.ANIME_MOVIES -> {
                    "Movies"
                }
                HomeAnimeScreen.WATCHED_ANIME -> {
                    "Watched"
                }
                HomeAnimeScreen.PROFILE -> {
                    "Profile"
                }
                HomeAnimeScreen.ABOUT_US -> {
                    "About Us"
                }
            }

            Text(text = text)
        },
        icon = {
            val icon = when(screen) {
                HomeAnimeScreen.ANIME_SERIES -> {
                    Icons.Outlined.Face
                }
                HomeAnimeScreen.ANIME_MOVIES -> {
                    Icons.Rounded.Lock
                }
                HomeAnimeScreen.WATCHED_ANIME -> {
                    Icons.Rounded.List
                }
                HomeAnimeScreen.PROFILE -> {
                    Icons.Rounded.AccountCircle
                }
                HomeAnimeScreen.ABOUT_US -> {
                    Icons.Rounded.Settings
                }
            }
            Icon(painter = rememberVectorPainter(image = icon), contentDescription = icon.name)
        }
    )
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
                AnimeLoader(
                    modifier = Modifier
                        .fillMaxSize(),
                    loaderGifUrl = LoaderUrls.PIKACHU.url
                )
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
            .defaultMinSize(minHeight = 200.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        if(segmentData.dataList.isEmpty()) {
            items(10) {
                ShimmeringAnimeCard()
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
                        navigateTo(AnimeScreen.DETAIL.name, animeData.id)
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
private fun LazyItemScope.ShimmeringAnimeCard() {
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

@Composable
private fun AnimeTitle(
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