package com.abhilash.apps.animecatalogue.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.abhilash.apps.animecatalogue.model.AnimeData
import com.abhilash.apps.animecatalogue.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val animeData = mutableStateOf<AnimeData?>(null)

    val categoryAttributeList = mutableStateListOf<String>()

    suspend fun getAnimeData(id: String) {
        repository.fetchAnimeById(id)?.let {
            animeData.value = it
            categoryAttributeList.addAll(it.genres.map { it.name })
        }
    }
}