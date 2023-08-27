package com.abhilash.apps.animecatalogue.model

import com.google.gson.annotations.SerializedName

data class PrimaryGenreData(
    val data: List<GenreData>
)

data class GenreData(
    @SerializedName("mal_id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("count")
    val count: String
)