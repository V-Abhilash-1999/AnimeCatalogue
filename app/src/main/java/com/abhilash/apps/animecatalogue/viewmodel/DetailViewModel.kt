package com.abhilash.apps.animecatalogue.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.abhilash.apps.animecatalogue.model.AnimeArticle
import com.abhilash.apps.animecatalogue.model.AnimeRepository
import com.abhilash.apps.animecatalogue.model.CategoryAttribute

class DetailViewModel: ViewModel() {
    private val repository = AnimeRepository()
    val animeArticle = mutableStateOf<AnimeArticle?>(null)

    val categoryAttributeList = mutableStateListOf<CategoryAttribute>()

    suspend fun getAnimeData(id: String) {
        repository.fetchAnimeWidthId(id)?.let {
            animeArticle.value = it.attributes
            getCategories(it.relationships.categories.links.self)
        }
    }

    private suspend fun getCategories(link: String) {
        repository.fetchCategory(link = link) {
            categoryAttributeList.add(it)
        }
    }
}