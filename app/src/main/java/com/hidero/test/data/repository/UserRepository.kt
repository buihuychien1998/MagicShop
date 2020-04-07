package com.hidero.test.data.repository

import com.hidero.test.data.api.APIService
import com.hidero.test.data.valueobject.Account
import io.reactivex.disposables.CompositeDisposable

class UserRepository(apiService: APIService, compositeDisposable: CompositeDisposable) {
    private var userDataSource = UserDataSource(apiService, compositeDisposable)
    val fetchUser =
        { username: String, password: String ->
            userDataSource.apply {
                fetchUser(username, password)
            }.downloadedAccountResponse
        }

    val register = { account: Account ->
        userDataSource.apply {
            register(account)
        }.downloadedRegisterResponse
    }

    val networkState = userDataSource.networkState

}