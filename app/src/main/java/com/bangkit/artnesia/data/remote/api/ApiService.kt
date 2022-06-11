package com.bangkit.artnesia.data.remote.api

import com.bangkit.artnesia.data.remote.response.LitResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api/literature")
    fun getLiterature(): Call<LitResponse>
}