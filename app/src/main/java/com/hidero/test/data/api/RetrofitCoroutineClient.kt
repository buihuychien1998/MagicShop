package com.hidero.test.data.api

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitCoroutineClient {
    companion object {
        private lateinit var retrofit: Retrofit
        @JvmStatic
        fun getClient(url: String): Retrofit {
            val requestInterceptor = Interceptor { chain ->
                // Interceptor take only one argument which is a lambda function so parenthesis can be omitted
                val httpUrl = chain.request()
                    .url()
                    .newBuilder()
//                    .addQueryParameter("api_key", API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(httpUrl)
                    .build()

                return@Interceptor chain.proceed(request)   //explicitly return a value from whit @ annotation. lambda always returns the value of the last expression implicitly
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build()
            val gson = GsonBuilder().setLenient().create()
            retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
            return retrofit
        }
    }

}