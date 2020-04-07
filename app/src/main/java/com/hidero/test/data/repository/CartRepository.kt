package com.hidero.test.data.repository

import com.hidero.test.data.api.APIService
import com.hidero.test.data.valueobject.Cart

class CartRepository(private val apiService: APIService): BaseRepository() {
    suspend fun getCart(username: String?): MutableList<Cart>?{
        return safeApiCall({ apiService.getCart(username)}, "Error Fetching")
    }

    suspend fun insertCart(username: String?, bookId: Int?, quantity: Int?, cost: Int?): String?{
        return safeApiCall({apiService.insertCart(username, bookId, quantity, cost)}, "Error Fetching")
    }

    suspend fun deleteCart(username: String?, bookId: Int?): String?{
        return safeApiCall({apiService.deleteCart(username, bookId)}, "Error Fetching")
    }

    suspend fun deleteAllCart(username: String?): String?{
        return safeApiCall({apiService.deleteAllCart(username)}, "Error Fetching")
    }

    suspend fun updateCart(username: String?, bookId: Int?, quantity: Int?, cost: Int?): String?{
        return safeApiCall({apiService.updateCart(username, bookId, quantity, cost)}, "Error Fetching")
    }
}