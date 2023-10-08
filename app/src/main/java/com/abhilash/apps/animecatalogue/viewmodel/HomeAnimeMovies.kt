package com.abhilash.apps.animecatalogue.viewmodel

import androidx.lifecycle.ViewModel
import com.abhilash.apps.animecatalogue.model.GenreData
import com.abhilash.apps.animecatalogue.model.Repository
import com.abhilash.apps.animecatalogue.view.screens.HomeSegment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class HomeAnimeMovies @Inject constructor(
    private val repository: Repository
): HomeViewModel() {

    override suspend fun fetchAllGenre(): List<GenreData>  = repository.fetchAllGenre()

    override suspend fun setCategoryData(homeSegment: HomeSegment) {
        _genreList.find { it.name.equals(homeSegment.category, true) }?.let { genreData ->
            repository.fetchMoviesByGenre(genreData.id)?.let { primaryData ->
                if(primaryData.data == null) {
                    delay(200)
                    setCategoryData(homeSegment)
                } else {
                    homeSegment.dataList.addAll(primaryData.data)
                }
            }
        }
    }
}