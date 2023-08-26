package com.abhilash.apps.animecatalogue.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhilash.apps.animecatalogue.model.AnimeArticle
import com.abhilash.apps.animecatalogue.model.AnimeData
import com.abhilash.apps.animecatalogue.model.AnimeRepository
import com.abhilash.apps.animecatalogue.model.BIG3
import com.abhilash.apps.animecatalogue.view.screens.HomeSegment
import com.abhilash.apps.animecatalogue.view.util.UIState
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val repository = AnimeRepository()

    //region Big 3
    val big3StateList = mutableStateListOf<AnimeArticle>()

    private val _uiState = mutableStateOf(UIState.LOADING)
    val uiState: State<UIState> = _uiState

    init {
        viewModelScope.launch {
            getBig3Data()
        }
    }

    private suspend fun getBig3Data() {
        BIG3.values().forEach {
            val data = repository
                .fetchAnimeWidthId(it.id)

            data?.let {
                if(_uiState.value == UIState.LOADING) {
                    _uiState.value = UIState.SUCCESS
                }
                big3StateList.add(it.attributes)
            } ?: run {
                _uiState.value = UIState.FAILURE
            }
        }
    }
    //endregion


    //region Category
    private suspend fun getCategory(category: String): List<AnimeData>? = repository.fetchAnimeWithCategory(category)

    suspend fun setCategoryData(homeSegment: HomeSegment) {
        getCategory(homeSegment.category)
            ?.forEach {
                homeSegment.dataList.add(it)
            }
    }
    //endregion
}