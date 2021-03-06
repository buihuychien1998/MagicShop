package com.hidero.test.ui.viewmodels

import com.example.mychatapp.notifications.Sender
import com.hidero.test.data.api.APIUtil
import com.hidero.test.data.repository.remote.CoroutineRepository
import com.hidero.test.ui.base.BaseViewModel
import com.hidero.test.util.baseUrl
import com.hidero.test.util.firebaseUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class MessageViewModel: BaseViewModel() {
    private val apiService by lazy {
        APIUtil.getCoroutineData(firebaseUrl)
    }
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO
    private val scope = CoroutineScope(coroutineContext)
    val repository = CoroutineRepository(apiService)
    fun sendNotification(body: Sender){
        scope.launch {
            try {
                repository.sendNotification(body)
            }catch (ex: Exception){
                Timber.e(ex)
            }
        }
    }
}