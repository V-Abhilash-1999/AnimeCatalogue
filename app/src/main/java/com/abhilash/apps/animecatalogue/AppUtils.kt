package com.abhilash.apps.animecatalogue

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

object AppUtils {
    suspend fun <T> Flow<T?>.collectLatestNonNull(callback: (T) -> Unit) {
        collectLatest { data ->
            data?.let(callback)
        }
    }
}