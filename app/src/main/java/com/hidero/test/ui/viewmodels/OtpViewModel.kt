package com.hidero.test.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.hidero.test.data.api.APIUtil
import com.hidero.test.data.repository.remote.CoroutineRepository
import com.hidero.test.ui.base.BaseViewModel
import com.hidero.test.util.baseUrl
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class OtpViewModel : BaseViewModel() {
    private val apiService by lazy {
        APIUtil.getCoroutineData(baseUrl)
    }
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO

    private val scope = CoroutineScope(coroutineContext)

    private val repository =
        CoroutineRepository(apiService)
    val status = MutableLiveData<String>()
    fun updateBill(username: String?) {
        scope.launch {
            try {
                status.postValue(repository.updateBill(username))
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
