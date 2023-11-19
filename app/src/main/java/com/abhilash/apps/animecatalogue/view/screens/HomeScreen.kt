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
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
import com.abhilash.apps.animecatalogue.viewmodel.HomeAnimeMovies
import com.abhilash.apps.animecatalogue.viewmodel.HomeAnimeSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val currentScreen = rememberSaveable {
        mutableStateOf(HomeAnimeScreen.ANIME_SERIES)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Anime Catalogue")
                }
            )
        },
        bottomBar = {
            CatalogueBottomBar(
                currentScreen = currentScreen.value,
                navigateToScreen = {
                    currentScreen.value = it
                    navController.navigate(it.name)
                }
            )
        }
    ) {
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            navController = navController,
            startDestination = HomeAnimeScreen.ANIME_SERIES.name
        ) {
            composable(HomeAnimeScreen.ANIME_SERIES.name) {
                val viewModel = hiltViewModel<HomeAnimeSeries>()
                val uiState by viewModel.uiState
                CatalogueScreen(
                    modifier = Modifier
                        .fillMaxSize(),
                    uiState = uiState
                ) { segment ->
                    viewModel.setCategoryData(segment)
                }
            }

            composable(HomeAnimeScreen.ANIME_MOVIES.name) {
                val viewModel = hiltViewModel<HomeAnimeMovies>()
                val uiState by viewModel.uiState
                CatalogueScreen(
                    modifier = Modifier
                        .fillMaxSize(),
                    uiState = uiState
                ) { segment ->
                    viewModel.setCategoryData(segment)
                }
            }


            composable(HomeAnimeScreen.WATCHED_ANIME.name) {
                WatchedAnimeScreen()
            }


            composable(HomeAnimeScreen.PROFILE.name) {

            }

            composable(HomeAnimeScreen.ABOUT_US.name) {

            }
        }
    }
}

@Composable
private fun CatalogueBottomBar(
    currentScreen: HomeAnimeScreen,
    navigateToScreen: (HomeAnimeScreen) -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        HomeAnimeScreen.values().forEach {
            NavigationItem(
                isCurrentScreen = currentScreen.name == it.name,
                screen = it
            ) {
                navigateToScreen(it)
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
    uiState: UIState,
    setCategoryData: suspend (HomeSegment) -> Unit
) {
    Box(modifier = modifier) {
        when (uiState) {
            UIState.SUCCESS -> {
                Catalogue(setCategoryData)
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
private fun Catalogue(
    setCategoryData: suspend (HomeSegment) -> Unit
) {
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
    LaunchedEffect(key1 = Unit) {
        launch(Dispatchers.IO) {
            segmentClass.forEach { segment ->
                setCategoryData(segment)
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
            AnimeCard(
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
                    .width(IntrinsicSize.Min),
                animeData = animeData
            )
        }
    }
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