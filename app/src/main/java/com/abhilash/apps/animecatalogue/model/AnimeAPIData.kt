package com.abhilash.apps.animecatalogue.model

import com.google.gson.annotations.SerializedName


data class PrimaryData(
    val pagination: PaginationData,
    val data: List<AnimeData>?
)

data class PrimaryAnimeData(
    val data: AnimeData
)

data class PaginationData(
    @SerializedName("current_page")
    val currentPage: String,

    @SerializedName("has_next_page")
    val hasNextPage: String,

    @SerializedName("last_visible_page")
    val lastVisiblePage: String
)

data class AnimeData(
    @SerializedName("mal_id")
    val id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("title_english")
    val englishTitle: String?,

    @SerializedName("title_japanese")
    val japaneseTitle: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("images")
    val images: ImageFormat,

    @SerializedName("titles")
    val titles: List<Titles>,

    @SerializedName("episodes")
    val episodes: Int,

    @SerializedName("score")
    val score: Float,

    @SerializedName("rating")
    val rating: String,

    @SerializedName("year")
    val releasedYear: Int,

    @SerializedName("synopsis")
    val description: String,

    @SerializedName("genres")
    val genres: List<AnimeGenreData>,

    @SerializedName("status")
    val runningStatus: String,

    @SerializedName("trailer")
    val trailer: YouTubeTrailer
)

data class YouTubeTrailer(
    @SerializedName("youtube_id")
    val videoId: String?,

    @SerializedName("embed_url")
    val embedUrl: String,

    @SerializedName("images")
    val images: ImageUrl
)

data class Titles(
    val type: String,
    val title: String
)

data class ImageFormat(
    val jpg: ImageUrl,
    val webp: ImageUrl
)

data class ImageUrl(
    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName("small_image_url")
    val smallImageUrl: String,

    @SerializedName("large_image_url")
    val largeImageUrl: String
)

data class AnimeGenreData(
    @SerializedName("mal_id")
    val id: Int,

    @SerializedName("name")
    val name: String
)