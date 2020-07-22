package com.hidero.test.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hidero.test.data.api.APIUtil
import com.hidero.test.data.repository.remote.CoroutineRepository
import com.hidero.test.ui.base.BaseViewModel
import com.hidero.test.util.DELAY_LOAD
import com.hidero.test.util.baseUrl
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SupportViewModel: BaseViewModel(){
    private val apiService by lazy {
        APIUtil.getCoroutineData(baseUrl)
    }
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO

    private val scope = CoroutineScope(coroutineContext)

    private val repository =
        CoroutineRepository(apiService)
    private val _numberBill = MutableLiveData<String>()
    val numberBill: LiveData<String>
        get() = _numberBill

    fun getNumberBills() {
        scope.launch {
            try {
                _numberBill.postValue(repository.getNumberBills())
            } catch (ex: Exception) {

            }
        }
    }

    fun resetData(){
        _numberBill.value = null
    }
    override fun onCleared() {
        super.onCleared()
    }
}