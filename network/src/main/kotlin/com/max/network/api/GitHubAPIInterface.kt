package com.max.network.api

import com.max.network.model.RateLimitResponse
import retrofit2.http.GET

interface GitHubAPIInterface {

    @GET("rate_limit")
    suspend fun getRateLimit(): RateLimitResponse
}