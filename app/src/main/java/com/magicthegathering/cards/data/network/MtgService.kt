package com.magicthegathering.cards.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MtgService {

    @GET("v1/cards")
    suspend fun searchCards(
        @Query("name") name: String?,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): CardsSuccessResponse

    object MtgApi {

        private const val BASE_URL = "https://api.magicthegathering.io/"

        fun create(): MtgService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL.toHttpUrl())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(MtgService::class.java)
        }

    }
}