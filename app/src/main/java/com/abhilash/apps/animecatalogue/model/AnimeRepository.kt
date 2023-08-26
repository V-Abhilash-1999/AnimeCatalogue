package com.abhilash.apps.animecatalogue.model

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.json.JSONStringer
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AnimeRepository {
    companion object {
         const val BASE_URL = "https://kitsu.io/api/edge/"
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

    suspend fun fetchAnimeWidthId(id:String): AnimeData? {
        return animeApi.getDataWithId(id).getSuccessfulDataOrNull()?.data?.firstOrNull()
    }

    suspend fun fetchAnimeWithCategory(category: String): List<AnimeData>? {
        val requestUrl = if(category == "*") {
            "${BASE_URL}anime/?filter[subtype]=TV&sort=-averageRating"
        } else {
            "${BASE_URL}anime/?include=categories&filter[categories]=$category&filter[subtype]=TV&sort=-averageRating"
        }
        val request = Request.Builder()
            .url("$requestUrl&page[limit]=20&page[offset]=0")
            .get()
            .build()

        val client = OkHttpClient
            .Builder()
            .build()

        val response = client.newCall(request).execute()
        return response.body?.string()?.let { responseString ->
            val gson = Gson()
            gson.fromJson(responseString, APIData::class.java).data
        }
    }

    suspend fun fetchCategory(link: String, onDataFetched: (CategoryAttribute) -> Unit) {
        val request = Request.Builder()
            .url(link)
            .get()
            .build()

        val client = OkHttpClient
            .Builder()
            .build()

        val response = client.newCall(request).execute()
        response.body?.string()?.let { responseString ->
            val gson = Gson()
            val categoryList = gson.fromJson(responseString, CategoryData::class.java).data

            categoryList.forEach { cat ->
                getCategoryAttribute(cat.id)?.let {
                    onDataFetched(it)
                }
            }
        }
    }

    suspend fun getCategoryAttribute(id: String): CategoryAttribute? {
        val request = Request.Builder()
            .url("${BASE_URL}categories/$id")
            .get()
            .build()

        val client = OkHttpClient
            .Builder()
            .build()

        val response = client.newCall(request).execute()
        return response.body?.string()?.let { responseString ->
            val jsonObj = JsonParser().parse(responseString).asJsonObject
            val jsonAttr = jsonObj.getAsJsonObject("data").getAsJsonObject("attributes")
            val gson = Gson()
            gson.fromJson(jsonAttr.toString(), CategoryAttribute::class.java)
        }

    }

    private fun <T> Response<T>.getSuccessfulDataOrNull(): T? {
        if(isSuccessful) {
            return body()
        }
        return null
    }
}