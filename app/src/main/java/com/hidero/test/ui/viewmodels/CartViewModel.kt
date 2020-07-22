package com.hidero.test.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.hidero.test.data.api.APIUtil
import com.hidero.test.data.repository.remote.CoroutineRepository
import com.hidero.test.data.valueobject.Account
import com.hidero.test.data.valueobject.Cart
import com.hidero.test.data.valueobject.Rating
import com.hidero.test.ui.base.BaseViewModel
import com.hidero.test.util.DELAY_LOAD
import com.hidero.test.util.baseUrl
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class CartViewModel : BaseViewModel() {
    private val apiService by lazy {
        APIUtil.getCoroutineData(baseUrl)
    }
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO

    private val scope = CoroutineScope(coroutineContext)

    private val repository =
        CoroutineRepository(apiService)
    private val _total = MutableLiveData<Float>()
    val total: LiveData<Float>
        get() = _total
    private val _rating = MutableLiveData<MutableList<Rating>>()
    val ratings: LiveData<MutableList<Rating>>
        get() = _rating

    private val _rate = MutableLiveData<Float>()
    val rate: LiveData<Float>
        get() = _rate
    private val _quantity = MutableLiveData<String>()
    val quantity: LiveData<String>
        get() = _quantity
    val cart = MutableLiveData<MutableList<Cart>>()
    val insertResponse = MutableLiveData<String>()
    fun fetchCart(username: String?) {
        scope.launch {
            try {
                if (username != null) {
                    delay(DELAY_LOAD)
                    val cart = repository.getCart(username)
                    updateTotal(cart)
                    this@CartViewModel.cart.postValue(cart)
                } else {
                    updateTotal(null)
                    cart.postValue(null)
                }

            } catch (ex: Exception) {

            }
        }
    }

    fun fetchUser(email: String?) {
        scope.launch {
            try {
                postAcc(repository.fetchUser(email))
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }
//
    val fetchUser = { email: String? ->
        liveData(Dispatchers.IO) {
            val acc = repository.fetchUser(email)
            emit(acc)
        }
    }

    fun insertCart(username: String?, bookId: Int?, quantity: Int?, cost: Int?) {
        scope.launch {
            try {
                val data = repository.insertCart(username, bookId, quantity, cost)
                updateTotal(cart.value)
                insertResponse.postValue(data)
            } catch (ex: Exception) {
            }
        }

    }

    fun deleteCart(username: String?, bookId: Int?) {
        scope.launch {
            try {
                repository.deleteCart(username, bookId)
                val cart = repository.getCart(username)
                updateTotal(cart)
                this@CartViewModel.cart.postValue(cart)
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }

    }

    fun deleteAllCart(username: String?) {
        scope.launch {
            try {
                repository.deleteAllCart(username)
                cart.postValue(null)
                updateTotal(null)
            } catch (ex: Exception) {

            }
        }

    }

    fun updateCart(username: String?, bookId: Int?, quantity: Int?, cost: Int?) {
        scope.launch {
            try {
                repository.updateCart(username, bookId, quantity, cost)
                updateTotal(cart.value)
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }

    }

    private fun updateTotal(cart: MutableList<Cart>?) {
        var tmp = 0F
        var q = 0
        cart?.forEach {
            it.quantity?.let { q1 -> q += q1 }
            tmp += it.quantity?.let { q -> it.cost?.times(q) } ?: 0
        }
        _total.postValue(tmp)
        _quantity.postValue("$q")
    }

    fun getRating(bookId: Int?) {
        scope.launch {
            try {
                _rating.postValue(repository.getRating(bookId))
                updateRate(ratings.value)
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }
    private fun updateRate(rate: MutableList<Rating>?) {
        var tmp = 0F
        rate?.forEach {
           it.rating?.apply {
               tmp += this
           }
        }
        rate?.size?.let {
            tmp /= it
        }
        _rate.postValue(tmp)
    }

    private fun cancelAllRequests() = coroutineContext.cancel()

    override fun onCleared() {
        super.onCleared()
        cancelAllRequests()
    }

}