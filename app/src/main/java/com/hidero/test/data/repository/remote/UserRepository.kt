package com.hidero.test.data.repository.remote

import com.example.mychatapp.notifications.MyResponse
import com.example.mychatapp.notifications.Sender
import com.hidero.test.data.api.APIService
import com.hidero.test.data.valueobject.Account
import io.reactivex.disposables.CompositeDisposable
import retrofit2.http.Body

class UserRepository(private val apiService: APIService, compositeDisposable: CompositeDisposable): BaseRepository() {
    private var userDataSource =
        UserDataSource(
            apiService,
            compositeDisposable
        )
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
    suspend fun sendNotification(@Body body: Sender?): MyResponse?{
        return safeApiCall({ apiService.sendNotification(body)}, "Error Fetching")
    }



    val networkState = userDataSource.networkState

}