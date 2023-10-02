package com.abhilash.apps.animecatalogue

data class RateLimitException(
    var status: String = "",
    val type: String = "",
    val message: String = "",
    val error: String = ""
)

const val RATE_LIMIT_EXCEPTION = "RateLimitException"