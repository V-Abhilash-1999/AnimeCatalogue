package com.abhilash.apps.animecatalogue.view

import android.media.Rating
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.abhilash.apps.animecatalogue.model.AnimeArticle
import com.abhilash.apps.animecatalogue.model.AnimeData
import com.abhilash.apps.animecatalogue.model.AnimeRepository
import com.abhilash.apps.animecatalogue.view.util.toPx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
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
        val uiState = remember {
            mutableStateOf(UIState.LOADING)
        }
        val dataList = remember {
            mutableStateListOf<AnimeData>()
        }
        LaunchedEffect(key1 = Unit) {
            launch(Dispatchers.IO) {
                val data = AnimeRepository().fetchAnime()
                if(data == null) {
                    uiState.value = UIState.FAILURE
                } else {
                    dataList.addAll(data.data)
                    uiState.value = UIState.SUCCESS
                }
            }
        }
        CatalogueScreen(
            modifier = Modifier
                .padding(it),
            uiState = uiState.value,
            dataList = dataList
        )
    }
}

@Composable
private fun CatalogueScreen(
    modifier: Modifier,
    uiState: UIState,
    dataList: SnapshotStateList<AnimeData>
) {
    Box(modifier = modifier) {
        when (uiState) {
            UIState.SUCCESS -> {
                Catalogue(dataList = dataList)
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
private fun LoadingScreen() {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
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
        modifier = Modifier.fillMaxSize(),
        painter = imagePainter,
        contentDescription = null
    )
}

@Composable
private fun Catalogue(
    dataList: SnapshotStateList<AnimeData>
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        state = rememberLazyGridState(),
        columns = GridCells.Fixed(2),
    ) {
        items(
            dataList,
            key = {
                it.id
            }
        ) { animeData ->
            AnimeCard(animeData.attributes)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyGridItemScope.AnimeCard(
    data: AnimeArticle
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .animateItemPlacement()
            .padding(8.dp),
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            AnimePoster(url = data.posterImage.originalImageUrl)
        }

        AnimeComponent(
            modifier = Modifier
                .fillMaxWidth(),
            data = data,
        )
    }
}

@Composable
private fun AnimePoster(url: String) {
    val imagePainter = rememberAsyncImagePainter(model = url)

    Image(
        modifier = Modifier
            .fillMaxSize(),
        painter = imagePainter,
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
    )
}

@Composable
private fun AnimeComponent(
    modifier: Modifier,
    data: AnimeArticle,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = data.canonicalTitle,
            style = LocalTextStyle.current
                .copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )

        AnimeRating(rating = data.averageRating)
    }
}

@Composable
fun AnimeRating(
    rating: Float
) {
    Row {
        Icon(
            imageVector = Icons.Default.Star,
            tint = Color.Yellow,
            contentDescription = null
        )


        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = String.format("%.2f", (rating * 0.05f)),
            style = LocalTextStyle.current
                .copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )


    }
}