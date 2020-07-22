package com.hidero.test.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hidero.test.data.api.APIUtil
import com.hidero.test.data.repository.remote.CoroutineRepository
import com.hidero.test.data.valueobject.Rating
import com.hidero.test.ui.base.BaseViewModel
import com.hidero.test.util.baseUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class SettingViewModel: BaseViewModel() {
    private val apiService by lazy {
        APIUtil.getCoroutineData(baseUrl)
    }
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO

    private val scope = CoroutineScope(coroutineContext)

    private val repository =
        CoroutineRepository(apiService)
    private val _imageStatus = MutableLiveData<String?>()
    val imageStatus: LiveData<String?>
        get() = _imageStatus

    fun updateImage(username: String?, url: String?) {
        scope.launch {
            try {
                Timber.e(repository.updateImage(username, url).toString())
                _imageStatus.postValue(repository.updateImage(username, url))
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }
}