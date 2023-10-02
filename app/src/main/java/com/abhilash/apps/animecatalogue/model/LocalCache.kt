package com.abhilash.apps.animecatalogue.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object LocalCache {
    private val cacheData = hashMapOf<String, CacheData>()
    class CacheData(
        val data: Any,
        val duration: CacheConfig
    ) {
        private val secondConvertorConstant = 60 * 1000L

        init {
            if(duration != CacheConfig.INFINITE) {
                scheduleClearCache()
            }
        }

        private fun scheduleClearCache() {
            CoroutineScope(Dispatchers.IO).launch {
                when(duration) {
                    CacheConfig.ONE_MINUTE -> {
                        clearCacheIn(1)
                    }
                    CacheConfig.FIVE_MINUTE -> {
                        clearCacheIn(5)
                    }
                    CacheConfig.TEN_MINUTE -> {
                        clearCacheIn(10)
                    }
                    else -> { }
                }
            }
        }

        private suspend fun clearCacheIn(seconds: Int) {
            delay(seconds * secondConvertorConstant)

            cacheData.filter { it.value == this@CacheData }.toList().firstOrNull()?.first?.let { key ->
                clearCacheOf(key)
            }
        }

    }

    enum class CacheConfig(val time: Int) {
        NO_CACHE(0),
        INFINITE(-1),
        ONE_MINUTE(1),
        FIVE_MINUTE(5),
        TEN_MINUTE(10),
    }

    fun <T> getCache(key: String): T? {
        return cacheData[key]?.data as? T
    }

    fun setCache(key: String, data: Any,  duration: CacheConfig) {
        cacheData[key] = CacheData(data = data, duration = duration)

    }

    private fun clearCacheOf(key:String) {
        cacheData.remove(key)
    }

    fun clearCache() {
        cacheData.clear()
    }

    val NO_CACHE: CacheConfig = CacheConfig.NO_CACHE
    val DEFAULT: CacheConfig = CacheConfig.INFINITE
}