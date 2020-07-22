package com.hidero.test.data.repository.remote

import com.example.mychatapp.notifications.Sender
import com.hidero.test.data.api.APIService
import com.hidero.test.data.valueobject.*
import com.hidero.test.ui.notifications.MyResponse
import retrofit2.http.Body

class CoroutineRepository(private val apiService: APIService) : BaseRepository() {
    suspend fun getNumberBills(): String? {
        return safeApiCall({ apiService.getNumberBills() }, "Error Fetching")
    }
    suspend fun getBill(username: String?): MutableList<Cart>? {
        return safeApiCall({ apiService.getBill(username) }, "Error Fetching")
    }
    suspend fun getCart(username: String?): MutableList<Cart>? {
        return safeApiCall({ apiService.getCart(username) }, "Error Fetching")
    }

    suspend fun insertCart(username: String?, bookId: Int?, quantity: Int?, cost: Int?): String? {
        return safeApiCall(
            { apiService.insertCart(username, bookId, quantity, cost) },
            "Error Fetching"
        )
    }

    suspend fun deleteCart(username: String?, bookId: Int?): String? {
        return safeApiCall({ apiService.deleteCart(username, bookId) }, "Error Fetching")
    }

    suspend fun deleteAllCart(username: String?): String? {
        return safeApiCall({ apiService.deleteAllCart(username) }, "Error Fetching")
    }

    suspend fun updateCart(username: String?, bookId: Int?, quantity: Int?, cost: Int?): String? {
        return safeApiCall(
            { apiService.updateCart(username, bookId, quantity, cost) },
            "Error Fetching"
        )
    }

    suspend fun getAuthor(): MutableList<Author>? {
        return safeApiCall({ apiService.getAuthor() }, "Error Fetching")
    }

    suspend fun getGenre(): MutableList<Genre>? {
        return safeApiCall({ apiService.getGenre() }, "Error Fetching")
    }
    suspend fun getRating(bookId: Int?): MutableList<Rating>? {
        return safeApiCall({ apiService.getRating(bookId) }, "Error Fetching")
    }
    suspend fun getStatistic(year: Int?): MutableList<Statistic>? {
        return safeApiCall({ apiService.statistic(year) }, "Error Fetching")
    }
    suspend fun updateImage(username: String?, url: String?): String? {
        return safeApiCall({ apiService.updateImage(username, url) }, "Error Fetching")
    }
    suspend fun updateBill(username: String?): String? {
        return safeApiCall({ apiService.updateBill(username) }, "Error Fetching")
    }

    suspend fun fetchUser(email: String?): Account? {
        return safeApiCall({ apiService.fetchUser(email) }, "Error Fetching")
    }

    suspend fun sendNotification(@Body body: Sender?): MyResponse? {
        return safeApiCall({ apiService.sendNotification(body) }, "Error Fetching")
    }


}