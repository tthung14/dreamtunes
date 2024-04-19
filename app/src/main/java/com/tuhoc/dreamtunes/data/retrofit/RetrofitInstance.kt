package com.tuhoc.dreamtunes.data.retrofit

import SongApi
import com.tuhoc.dreamtunes.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private val instance: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val getSongApi: SongApi = instance.create(SongApi::class.java)
    }
}