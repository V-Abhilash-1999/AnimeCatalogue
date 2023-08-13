package com.abhilash.apps.animecatalogue.model

import android.util.Log
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET


class APIData(
    val data: List<AnimeData>
)

class AnimeData(
    val id: String,
    val attributes: AnimeArticle
)

class AnimeArticle(
    val canonicalTitle: String,
    val averageRating: Float,
    val synopsis: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val posterImage: AnimePoster
)

class AnimePoster(
    @SerializedName("tiny")
    val tinyImageUrl: String,

    @SerializedName("small")
    val smallImageUrl: String,

    @SerializedName("medium")
    val mediumImageUrl: String,

    @SerializedName("large")
    val largeImageUrl: String,

    @SerializedName("original")
    val originalImageUrl: String,
)
