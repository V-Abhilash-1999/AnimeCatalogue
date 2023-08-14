package com.abhilash.apps.animecatalogue.view

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.abhilash.apps.animecatalogue.model.AnimeArticle
import com.abhilash.apps.animecatalogue.model.AnimeData
import com.abhilash.apps.animecatalogue.model.AnimeRepository
import com.abhilash.apps.animecatalogue.view.util.AutoScrollHelper
import com.abhilash.apps.animecatalogue.view.util.Separator
import com.abhilash.apps.animecatalogue.view.util.drawBottomShade
import com.abhilash.apps.animecatalogue.view.util.pagerTabIndicatorOffset
import com.abhilash.apps.animecatalogue.view.util.timeLine
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
                .fillMaxSize()
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
private fun BoxScope.LoadingScreen() {
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
        modifier = Modifier
            .size(72.dp)
            .align(Alignment.Center),
        painter = imagePainter,
        contentDescription = null
    )
}

@Composable
private fun Catalogue(
    dataList: SnapshotStateList<AnimeData>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AutoScrollingViewPager(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        )

        Separator()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AutoScrollingViewPager(
    modifier: Modifier
) {
    val big3 = listOf("12","1555", "244")

    val big3DataList = remember {
        mutableStateListOf<AnimeArticle>()
    }

    //VMRequirement
    LaunchedEffect(key1 = Unit) {
        big3.forEach {
            AnimeRepository().fetchAnimeWidthId(it)?.let {
                big3DataList.add(it)
            }
        }
    }

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
            big3.forEach { _ ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .height(4.dp)
                        .width(24.dp)
                        .background(Color.LightGray, RoundedCornerShape(100))
                )
            }

        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    AutoScrollHelper(
        pagerState = pagerState,
        size = big3.size
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
        AnimePoster(url = article.posterImage.originalImageUrl)

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

@Composable
private fun AnimePoster(url: String) {
    val imagePainter = rememberAsyncImagePainter(model = url)

    Image(
        modifier = Modifier
            .fillMaxSize(),
        painter = imagePainter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}