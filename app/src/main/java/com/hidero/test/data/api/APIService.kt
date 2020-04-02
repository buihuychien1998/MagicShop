package com.hidero.test.data.api

import com.hidero.test.data.valueobject.Account
import com.hidero.test.data.valueobject.Book
import com.hidero.test.data.valueobject.Cart
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*


interface APIService {
    @GET("getBook.php")
    fun getBook(@Query("page") page: Int,
                @Query("pageSize") pageSize: Int): Single<MutableList<Book>>
    @GET("getBook.php")
    fun search(@Query("keyword") keyword: String, @Query("page") page: Int,
               @Query("pageSize") pageSize: Int): Single<MutableList<Book>>
    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Single<Account>

    @FormUrlEncoded
    @POST("register.php")
    fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("role") role: Int,
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("phone") phone: String,
        @Field("email") email: String
    ): Single<String>

    @FormUrlEncoded
    @POST("getCart.php")
    suspend fun getCart(
        @Field("username") username: String
    ): Response<MutableList<Cart>>
}