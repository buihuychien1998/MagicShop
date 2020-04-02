package com.hidero.test.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hidero.test.data.api.APIUtil
import com.hidero.test.data.repository.UserRepository
import com.hidero.test.data.valueobject.Account
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.util.baseUrl
import io.reactivex.disposables.CompositeDisposable

class UserViewModel : ViewModel() {
    private val apiService by lazy {
        APIUtil.getData(baseUrl)
    }
    private val compositeDisposable = CompositeDisposable()

    private val repository by lazy {
        UserRepository(apiService, compositeDisposable)
    }
    var user: LiveData<Account>? = null

    var result: LiveData<String>? = null

    val networkState: LiveData<NetworkState> by lazy {
        repository.networkState
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    val fetchUser = { username: String, password: String ->
        repository.fetchUser(username, password)
    }

    val register = { username: String,
                     password: String, name: String, address: String, phone: String, email: String ->
        repository.register(username, password, name, address, phone, email)
    }
}