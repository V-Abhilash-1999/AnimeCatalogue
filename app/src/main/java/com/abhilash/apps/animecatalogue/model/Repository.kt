package com.abhilash.apps.animecatalogue.model

import com.google.gson.Gson
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.Request


class Repository private constructor() {
    companion object {
        const val BASE_URL = "https://api.jikan.moe/v4/"
        private var instance: Repository? = null
        fun instance(): Repository {
            if(instance == null) {
                instance = Repository()
            }

            return instance!!
        }
    }

    private val httpClient = OkHttpClient
        .Builder()
        .build()

    private val gson = Gson()


    suspend fun fetchAllGenre(): List<GenreData> {
        return (makeAPICall("${BASE_URL}genres/anime", PrimaryGenreData::class.java)?.data?.toMutableList() ?: mutableListOf()).also {
            it.add(
                GenreData(
                    id = "",
                    name = "Popular",
                    url = "",
                    count = "-1"
                )
            )
        }
    }

    suspend fun fetchAnimeById(id: String): AnimeData? {
        return makeAPICall("${BASE_URL}anime/$id", PrimaryAnimeData::class.java)?.data
    }

    suspend fun fetchAnimeByGenre(genreID: String): PrimaryData? {
        delay(300)
        return makeAPICall("${BASE_URL}anime?genres=$genreID&type=tv&order_by=score&sort=desc", PrimaryData::class.java)
    }


    suspend fun <T> makeAPICall(url: String, classOfT: Class<T>): T? {
        val request = Request.Builder()
            .url(url)
            .build()

        return httpClient.newCall(request).execute().body?.string()?.let { response ->
            gson.fromJson(response, classOfT)
        }
    }
}