package com.abhilash.apps.animecatalogue.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimeAPI {
    @GET("anime/")
    suspend fun getData(): Response<APIData>


    @GET("anime/?sort=-favoritesCount&?page[limit]=20&page[offset]={currentSize}")
    suspend fun getPopularAnime(currentSize: Int = 0): Response<APIData>


    @GET("anime/")
    suspend fun getDateWithId(@Query("filter[id]") id: String): Response<APIData>
}