package com.hidero.test.data.api

import android.accounts.Account
import com.hidero.book.model.Cart
import com.hidero.book.model.Species
import com.hidero.test.data.valueobject.Author
import com.hidero.test.data.valueobject.Book
import retrofit2.Call
import retrofit2.http.*


interface APIService {
    @FormUrlEncoded
    @POST("login.php")
    fun loginData(
        @Field("username") username: String,
        @Field("password") password: String
    ):
    //    Convert json
            Call<List<Account>>

    @GET("author.php")
    fun authorData(): Call<List<Author>>

    @GET("species.php")
    fun speciesData(): Call<List<Species>>

    //  Muốn sử dụng phương thức POST gửi dữ liệu dưới dạng chuỗi thì dùng phương thức này
    @FormUrlEncoded
    @POST("register.php")
//    Nhận dữ liệu từ server mà server trả về chuỗi (file insert phần echo)
    fun registerData(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("role") role: Int,
        @Field("fullname") fullname: String,
        @Field("address") address: String,
        @Field("phone") phone: String,
        @Field("email") email: String
    ): Call<String>


    @FormUrlEncoded
    @POST("updateAccount.php")
    fun changePasswordData(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("newPassword") newPassword: String
    ): Call<String>

    @FormUrlEncoded
    @POST("getCart.php")
    fun getCartData(
        @Field("username") username: String
    ): Call<List<Cart>>

    //  Muốn sử dụng phương thức POST gửi dữ liệu dưới dạng chuỗi thì dùng phương thức này
    @FormUrlEncoded
    @POST("insertCart.php")
//    Nhận dữ liệu từ server mà server trả về chuỗi (file insert phần echo)
    fun insertCartData(
        @Field("username") username: String,
        @Field("idBook") idBook: Int,
        @Field("quantity") quantity: Int,
        @Field("cost") cost: Int,
        @Field("total") total: Int
    ): Call<String>

    @GET("deleteCart.php")
//    Nhận dữ liệu từ server mà server trả về chuỗi (file insert phần echo)
    fun deleteCartData(
        @Query("username") username: String,
        @Query("idBook") idBook: Int
    ): Call<String>

    //  Muốn sử dụng phương thức POST gửi dữ liệu dưới dạng chuỗi thì dùng phương thức này
    @FormUrlEncoded
    @POST("updateCart.php")
//    Nhận dữ liệu từ server mà server trả về chuỗi (file insert phần echo)
    fun updateCartData(
        @Field("username") username: String,
        @Field("idBook") idBook: Int,
        @Field("quantity") quantity: Int,
        @Field("cost") cost: Int,
        @Field("total") total: Int
    ): Call<String>

    @FormUrlEncoded
    @POST("updateBill.php")
//    Nhận dữ liệu từ server mà server trả về chuỗi (file insert phần echo)
    fun updateBillData(
        @Field("username") username: String
    ): Call<String>

    @FormUrlEncoded
    @POST("filter.php")
//    Nhận dữ liệu từ server mà server trả về chuỗi (file insert phần echo)
    fun filterData(
        @Field("idSpecies") idSpecies: Int?,
        @Field("idAuthor") idAuthor: Int?
    ): Call<List<Book>>

}