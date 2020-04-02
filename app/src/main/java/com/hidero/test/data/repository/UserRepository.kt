package com.hidero.test.data.repository

import com.hidero.test.data.api.APIService
import io.reactivex.disposables.CompositeDisposable

class UserRepository(apiService: APIService, compositeDisposable: CompositeDisposable) {
    private var userDataSource = UserDataSource(apiService, compositeDisposable)
    val fetchUser =
        { username: String, password: String ->
            userDataSource.apply {
                fetchUser(username, password)
            }.downloadedAccountResponse
        }

    val register = { username: String,
                     password: String, name: String, address: String, phone: String, email: String ->
        userDataSource.apply {
            register(username, password, name, address, phone, email)
        }.downloadedRegisterResponse
    }

    val networkState = userDataSource.networkState

}