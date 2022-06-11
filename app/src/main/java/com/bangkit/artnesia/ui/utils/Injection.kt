package com.bangkit.artnesia.ui.utils

import android.content.Context
import com.bangkit.artnesia.data.remote.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        val appExecutors = AppExecutors()

        return Repository.getInstance(apiService, appExecutors)
    }
}