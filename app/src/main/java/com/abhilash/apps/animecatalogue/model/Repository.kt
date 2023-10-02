package com.abhilash.apps.animecatalogue.model

import com.abhilash.apps.animecatalogue.RATE_LIMIT_EXCEPTION
import com.abhilash.apps.animecatalogue.RateLimitException
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
        .connectTimeout(5, TimeUnit.MINUTES)
        .writeTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
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

    private suspend fun <T> makeAPICall(url: String, classOfT: Class<T>, cacheConfig: LocalCache.CacheConfig = LocalCache.DEFAULT): T? {
        LocalCache.getCache<T>(url)?.let {
            return it
        }

        val request = Request.Builder()
            .url(url)
            .cacheControl(cacheControl)
            .build()


        return httpClient.newCall(request).execute().body?.string()?.let { response ->
            val exception = gson.fromJson(response, RateLimitException::class.java)
            if(exception.type == RATE_LIMIT_EXCEPTION) {
                delay(100)
                return makeAPICall(url, classOfT, cacheConfig)
            }
            val responseData = gson.fromJson(response, classOfT)
            if(cacheConfig != LocalCache.NO_CACHE) {
                LocalCache.setCache(url, responseData as Any, cacheConfig)
            }
            responseData
        } ?: kotlin.run {
            delay(100)
            makeAPICall(url, classOfT, cacheConfig)
        }
    }
}