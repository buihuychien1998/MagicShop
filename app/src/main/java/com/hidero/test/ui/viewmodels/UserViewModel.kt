package com.hidero.test.ui.viewmodels

import androidx.lifecycle.LiveData
import com.hidero.test.data.api.APIUtil
import com.hidero.test.data.repository.UserRepository
import com.hidero.test.data.valueobject.Account
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.ui.base.BaseViewModel
import com.hidero.test.util.baseUrl
import io.reactivex.disposables.CompositeDisposable

class UserViewModel : BaseViewModel() {
    private val apiService by lazy {
        APIUtil.getData(baseUrl)
    }
    private val compositeDisposable = CompositeDisposable()

    private val repository by lazy {
        UserRepository(apiService, compositeDisposable)
    }
    var user: LiveData<Account>? = null

    var result: LiveData<String>? = null

    var networkState: LiveData<NetworkState> = repository.networkState


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    val fetchUser = { username: String, password: String ->
        repository.fetchUser(username, password)
    }

    val register = { account: Account ->
        repository.register(account)
    }
}