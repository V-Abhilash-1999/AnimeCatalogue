package com.abhilash.apps.animecatalogue.model

import com.google.gson.Gson
import kotlinx.coroutines.delay
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Repository @Inject constructor() {
    companion object {
        private const val BASE_URL = "https://api.jikan.moe/v4/"
    }

    private val httpClient = OkHttpClient
        .Builder()
        .cache(Cache(File("/cache"), 100 * 1024 * 1024))
        .build()

    private val gson = Gson()

    private var authToken: String? = null

    fun updateAuthToken(token: String?) {
        authToken = token
    }


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
        return makeAPICall("${BASE_URL}anime?genres=$genreID&type=tv&order_by=score&sort=desc", PrimaryData::class.java)
    }

    private val cacheControl = CacheControl.Builder()
        .maxAge(1, TimeUnit.HOURS)
        .build()


    private suspend fun <T> makeAPICall(url: String, classOfT: Class<T>): T? {
        val request = Request.Builder()
            .url(url)
            .cacheControl(cacheControl)
            .build()


        return httpClient.newCall(request).execute().body?.string()?.let { response ->
            gson.fromJson(response, classOfT)
        } ?: kotlin.run {
            delay(100)
            makeAPICall(url, classOfT)
        }
    }
}