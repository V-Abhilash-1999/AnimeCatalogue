package com.abhilash.apps.animecatalogue.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhilash.apps.animecatalogue.model.GenreData
import com.abhilash.apps.animecatalogue.view.screens.HomeSegment
import com.abhilash.apps.animecatalogue.view.util.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class HomeViewModel: ViewModel() {

    protected val _genreList = mutableListOf<GenreData>()

    val genreData: List<GenreData>
        get() = _genreList


    private val _uiState = mutableStateOf(UIState.LOADING)
    val uiState: State<UIState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetchAllGenre().let {
                launch(Dispatchers.Main.immediate) {
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
    }

    abstract suspend fun fetchAllGenre(): List<GenreData>

    abstract suspend fun setCategoryData(homeSegment: HomeSegment)


}