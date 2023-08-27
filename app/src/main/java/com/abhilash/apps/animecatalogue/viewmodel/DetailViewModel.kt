package com.abhilash.apps.animecatalogue.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.abhilash.apps.animecatalogue.model.AnimeData
import com.abhilash.apps.animecatalogue.model.Repository

class DetailViewModel: ViewModel() {
    private val repository = Repository.instance()
    val animeData = mutableStateOf<AnimeData?>(null)

    val categoryAttributeList = mutableStateListOf<String>()

    suspend fun getAnimeData(id: String) {
        repository.fetchAnimeById(id)?.let {
            animeData.value = it
            categoryAttributeList.addAll(it.genres.map { it.name })
        }
    }
}