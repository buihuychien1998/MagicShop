package com.hidero.test.data.repository.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hidero.test.data.api.APIService
import com.hidero.test.data.valueobject.Account
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.util.USER_ROLE
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class UserDataSource(
    private val apiService: APIService,
    private val compositeDisposable: CompositeDisposable
) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState                   //with this get, no need to implement get function to get networkSate

    private val _downloadedAccountResponse = MutableLiveData<Account>()
    val downloadedAccountResponse: LiveData<Account>
        get() = _downloadedAccountResponse

    private val _downloadedRegisterResponse = MutableLiveData<String>()
    val downloadedRegisterResponse: LiveData<String>
        get() = _downloadedRegisterResponse

    fun fetchUser(username: String, password: String) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.login(username, password)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        { acc ->
                            _downloadedAccountResponse.postValue(acc)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                        }

                    )
            )

        } catch (e: Exception) {
            Log.e("LoginDataSource Ex", e.message.toString())
        }
    }

    fun register(account: Account) {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                account.run {
                    apiService.register(username, password, USER_ROLE, name, address, phone, email)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                            { response ->
                                _downloadedRegisterResponse.postValue(response)
                                _networkState.postValue(NetworkState.LOADED)
                            },
                            {
                                _networkState.postValue(NetworkState.ERROR)
                            }

                        )
                }

            )

        } catch (e: Exception) {
            Log.e("RegisterDataSource Ex", e.message.toString())
        }
    }
}