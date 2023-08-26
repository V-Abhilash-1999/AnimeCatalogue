package com.abhilash.apps.animecatalogue.model

import com.google.gson.annotations.SerializedName


class APIData(
    val data: List<AnimeData>
)

class AnimeData(
    val id: String,
    val attributes: AnimeArticle,
    val relationships: Relationship
)

class Relationship(
    val genres: APILinkHolder,
    val categories: APILinkHolder
)

class APILinkHolder(
    val links: APILink
)

class APILink(
    val self: String,
    val related: String
)

class AnimeArticle(
    val canonicalTitle: String,
    val averageRating: Float,
    val synopsis: String,
    val description: String,
    val startDate: String,
    val endDate: String?,
    val ageRating: String,
    val posterImage: AnimePosterImageUrl,
    val coverImage: AnimePosterImageUrl,
    val episodeLength: Int,
    val totalLength: Int,
    val status: String
)

class AnimePosterImageUrl(
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


class CategoryData(
    val data: List<Category>
)

class Category(
    val type: String,
    val id: String
)

class CategoryAttribute(
    val title: String,
    val description: String
)