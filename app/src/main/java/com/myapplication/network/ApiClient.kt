package com.myapplication.network

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val retrofit: Retrofit by lazy {
    Retrofit.Builder().baseUrl("https://demo1354773.mockable.io/")
        .client(okHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

}

fun okHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor())
        .addInterceptor {
            val originalRequest = it.request()
            val builder = originalRequest.newBuilder()
            val newRequest = builder.build()
            it.proceed(newRequest)
        }
        .readTimeout(5000, TimeUnit.MILLISECONDS)
        .connectTimeout(10000, TimeUnit.MILLISECONDS)
        .build()
}

fun loggingInterceptor(): HttpLoggingInterceptor {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return loggingInterceptor
}