package com.abhilash.apps.animecatalogue.model

import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AnimeRepository {
    companion object {
         private const val BASE_URL = "https://kitsu.io/api/edge/"
    }
    private var gson = GsonBuilder()
        .setLenient()
        .create()
    private val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            GsonConverterFactory
                .create(gson)
        )
        .build()
    private val animeApi = retrofit.create(AnimeAPI::class.java)

    suspend fun fetchAnime(): APIData? {
        return animeApi.getData().getSuccessfulDataOrNull()
    }

    suspend fun fetchAnimeWidthId(id:String): AnimeArticle? {
        return animeApi.getDateWithId(id).getSuccessfulDataOrNull()?.data?.firstOrNull()?.attributes
    }


    private fun <T> Response<T>.getSuccessfulDataOrNull(): T? {
        if(isSuccessful) {
            return body()
        }
        return null
    }
}