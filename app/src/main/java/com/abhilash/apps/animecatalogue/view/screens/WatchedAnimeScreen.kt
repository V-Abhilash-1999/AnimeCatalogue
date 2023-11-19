package com.abhilash.apps.animecatalogue.view.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abhilash.apps.animecatalogue.view.theme.LocalNavigateToLambda

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WatchedAnimeScreen() {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(3),
    ) {
        items(100) {
            val navigateTo = LocalNavigateToLambda.current
            /*AnimeCard(
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
                        navigateTo(AnimeScreen.DETAIL.name, "")
                    }
                    .padding(8.dp)
                    .animateItemPlacement()
                    .width(IntrinsicSize.Min),
                animeData =
            )*/
        }
    }
}