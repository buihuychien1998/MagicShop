package com.hidero.test.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hidero.test.data.api.APIUtil
import com.hidero.test.data.repository.CartRepository
import com.hidero.test.data.valueobject.Cart
import com.hidero.test.util.baseUrl
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CartViewModel: ViewModel() {
    private val apiService by lazy {
        APIUtil.getCoroutineData(baseUrl)
    }
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : CartRepository = CartRepository(apiService)
    val cart = MutableLiveData<MutableList<Cart>>()
    fun fetchCart(username: String){

        scope.launch {
            try{
                val cartList = repository.getCart(username)
                cart.postValue(cartList)
            }catch (ex: Exception){

            }
        }
    }

    private fun cancelAllRequests() = coroutineContext.cancel()

    override fun onCleared() {
        super.onCleared()
        cancelAllRequests()
    }
}