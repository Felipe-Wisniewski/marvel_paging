package com.wisnitech.marvelpaging.repository.service

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceImpl {
    private const val BASE_URL = "https://gateway.marvel.com/v1/public/"

    fun getService(): ApiService {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->

                val original = chain.request()
                val originalHttpUrl = original.url

                val timeStamp = System.currentTimeMillis().toString()

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("ts", timeStamp)
                    .addQueryParameter("apikey", PUBLIC_API_KEY)
                    .addQueryParameter("hash", getHash(timeStamp))
                    .build()

                chain.proceed(original.newBuilder().url(url).build())
            }

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client.build())
            .build()

        return retrofit.create<ApiService>(ApiService::class.java)
    }
}