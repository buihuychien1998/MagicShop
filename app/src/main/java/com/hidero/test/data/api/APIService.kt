package com.hidero.test.data.api

import com.hidero.test.ui.notifications.MyResponse
import com.example.mychatapp.notifications.Sender
import com.hidero.test.data.valueobject.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*


interface APIService {
    @GET("getBook.php")
    fun getBook(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Single<MutableList<Book>>

    @GET("getBook.php")
    fun search(
        @Query("keyword") keyword: String, @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Single<MutableList<Book>>

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Single<Account>

    @GET("fetchUser.php")
    suspend fun fetchUser(
        @Query("email") email: String?
    ): Response<Account>

    @FormUrlEncoded
    @POST("register.php")
    fun register(
        @Field("username") username: String?,
        @Field("password") password: String?,
        @Field("role") role: Int?,
        @Field("name") name: String?,
        @Field("address") address: String?,
        @Field("phone") phone: String?,
        @Field("email") email: String?
    ): Single<String>

    @FormUrlEncoded
    @POST("getCart.php")
    suspend fun getCart(
        @Field("username") username: String?
    ): Response<MutableList<Cart>>

    @FormUrlEncoded
    @POST("insertCart.php")
    suspend fun insertCart(
        @Field("username") username: String?,
        @Field("bookId") bookId: Int?,
        @Field("quantity") quantity: Int?,
        @Field("cost") cost: Int?
    ): Response<String>

    @GET("deleteCart.php")
    suspend fun deleteCart(
        @Query("username") username: String?,
        @Query("bookId") bookId: Int?
    ): Response<String>

    @GET("deleteAllCart.php")
    suspend fun deleteAllCart(
        @Query("username") username: String?
    ): Response<String>

    @FormUrlEncoded
    @POST("updateCart.php")
    suspend fun updateCart(
        @Field("username") username: String?,
        @Field("bookId") bookId: Int?,
        @Field("quantity") quantity: Int?,
        @Field("cost") cost: Int?
    ): Response<String>

    @GET("author.php")
    suspend fun getAuthor(): Response<MutableList<Author>>

    @GET("genre.php")
    suspend fun getGenre(): Response<MutableList<Genre>>

    @GET("filter.php")
    fun filter(
        @Query("genreId") genreId: Int, @Query("authorId") authorId: Int
        , @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<MutableList<Book>>

    @GET("updateBill.php")
    suspend fun updateBill(): Response<String>

    @POST("fcm/send")
    suspend fun sendNotification(@Body body: Sender?): Response<MyResponse>
}