package com.example.emoji_memory_android.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

data class EmojiDto(
    val name: String = "",
    val unicode: List<String> = emptyList()
)

interface EmojiApiService {

    @GET("api/all/category/{categoria}")
    suspend fun buscarPorCategoria(@Path("categoria") categoria: String): List<EmojiDto>
}

object EmojiApi {

    private const val BASE_URL = "https://emojihub.yurace.pro/"

    val service: EmojiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmojiApiService::class.java)
    }
}
