package com.example.rickandmorty

import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val certificatePinner = CertificatePinner.Builder()
        .add(
            "rickandmortyapi.com",
            "sha256/4HQtdsbE14lqcEw5YHG4JVx5rHWfpCmS09cZ0yfVd4U="
        )
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .certificatePinner(certificatePinner)
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .callTimeout(120, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: RickAndMortyApi =
        retrofit.create(RickAndMortyApi::class.java)
}
