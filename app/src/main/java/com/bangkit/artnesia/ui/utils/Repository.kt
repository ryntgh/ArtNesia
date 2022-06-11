package com.bangkit.artnesia.ui.utils

import com.bangkit.artnesia.data.remote.api.ApiService
import com.bangkit.artnesia.data.remote.response.LitResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository(
    private val apiService: ApiService,
    val appExecutors: AppExecutors
) {

    fun getLiteratureData(): Call<LitResponse> {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://34.121.31.156/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        val apiService = retrofit.create(ApiService::class.java)
        return apiService.getLiterature()
    }

    companion object {
        @Volatile
        private var instance: Repository? = null

        @JvmStatic
        fun getInstance(
            apiService: ApiService,
            appExecutors: AppExecutors
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, appExecutors)
            }.also { instance = it }
    }
}