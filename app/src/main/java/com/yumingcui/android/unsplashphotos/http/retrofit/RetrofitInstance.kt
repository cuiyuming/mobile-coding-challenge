package com.yumingcui.android.unsplashphotos.http.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient



object RetrofitInstance {

    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://api.unsplash.com/"

    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                val httpClient = OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("Authorization", "Client-ID 732bece8be02158d8828145d641b1b96ab789da69174a78637387b427a48f123").build()
                        chain.proceed(request)
                    }.build()

                retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build()
            }
            return retrofit
        }

}

