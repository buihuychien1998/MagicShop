package com.hidero.test.ui.base

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hidero.test.data.valueobject.Account
import com.hidero.test.ui.viewmodels.Event
import com.hidero.test.util.CURRENT_USER
import com.hidero.test.util.SharedPrefs

abstract class BaseViewModel : ViewModel() {
    private val _navigateTo = MutableLiveData<Event<View>>()
    val navigateTo: LiveData<Event<View>>
        get() = _navigateTo
    private val _account =
        MutableLiveData(SharedPrefs.instance[CURRENT_USER, Account::class.java])
    val account: LiveData<Account?>
        get() = _account

    fun refreshAccount(){
        _account.postValue(SharedPrefs.instance[CURRENT_USER, Account::class.java])
    }

    fun navigateTo(view: View) {
        _navigateTo.value = Event(view)  // Trigger the event by setting a new Event as a new value
    }
}