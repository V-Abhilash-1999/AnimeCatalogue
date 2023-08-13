package com.abhilash.apps.animecatalogue.model

import com.google.gson.GsonBuilder
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

    suspend fun fetchAnime(): APIData? {
        val animeApi = retrofit.create(AnimeAPI::class.java)
        val response = animeApi.getData()
        if(response.isSuccessful) {
            return response.body()
        }
        return null
    }
}