package com.hidero.test.data.repository

import com.hidero.test.data.api.APIService
import com.hidero.test.data.valueobject.Cart

class CartRepository(private val apiService: APIService): BaseRepository() {
    suspend fun getCart(username: String): MutableList<Cart>?{
        return safeApiCall({ apiService.getCart(username)}, "Error Fetching")
    }
}