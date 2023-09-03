package com.abhilash.apps.animecatalogue.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhilash.apps.animecatalogue.model.GenreData
import com.abhilash.apps.animecatalogue.model.Repository
import com.abhilash.apps.animecatalogue.view.screens.HomeSegment
import com.abhilash.apps.animecatalogue.view.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {
    private val _genreList = mutableListOf<GenreData>()

    val genreData: List<GenreData>
        get() = _genreList

    //region UI State

    private val _uiState = mutableStateOf(UIState.LOADING)
    val uiState: State<UIState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchAllGenre().let {
                _genreList.clear()
                _genreList.addAll(it)
                if(it.isEmpty()) {
                    _uiState.value = UIState.FAILURE
                } else {
                    if(_uiState.value == UIState.LOADING) {
                        _uiState.value = UIState.SUCCESS
                    }
                }
            }

        }
    }

    //endregion


    //region Category
    suspend fun setCategoryData(homeSegment: HomeSegment) {
        _genreList.find { it.name.equals(homeSegment.category, true) }?.let { genreData ->
            repository.fetchAnimeByGenre(genreData.id)?.let { primaryData ->
                if(primaryData.data == null) {
                    delay(200)
                    setCategoryData(homeSegment)
                } else {
                    homeSegment.dataList.addAll(primaryData.data)
                }
            }
        }
    }


    //endregion
}