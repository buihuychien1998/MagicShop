package com.hidero.test.data.api

import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitClient {

    companion object {
        private lateinit var retrofit: Retrofit
//        fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
//            return try { // Create a trust manager that does not validate certificate chains
//                val trustAllCerts = arrayOf<TrustManager>(
//                    object : X509TrustManager {
//                        @SuppressLint("TrustAllX509TrustManager")
//                        @Throws(CertificateException::class)
//                        override fun checkClientTrusted(
//                            chain: Array<X509Certificate>,
//                            authType: String
//                        ) {
//                        }
//
//                        @Throws(CertificateException::class)
//                        override fun checkServerTrusted(
//                            chain: Array<X509Certificate>,
//                            authType: String
//                        ) {
//                        }
//
//                        override fun getAcceptedIssuers(): Array<X509Certificate> {
//                            return arrayOf()
//                        }
//                    }
//                )
//                // Install the all-trusting trust manager
//                val sslContext = SSLContext.getInstance("SSL")
//                sslContext.init(null, trustAllCerts, SecureRandom())
//                // Create an ssl socket factory with our all-trusting manager
//                val sslSocketFactory = sslContext.socketFactory
//                val builder = OkHttpClient.Builder()
//                builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
//                builder.hostnameVerifier { hostname, session -> true }
//                builder
//            } catch (e: Exception) {
//                throw RuntimeException(e)
//            }
//        }
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
            return retrofit
        }
    }

}