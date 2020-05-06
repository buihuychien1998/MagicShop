package com.hidero.test.data.valueobject

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class Listing<T>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>, //initial state
    val refreshState: LiveData<NetworkState>, // second state, after first data loaded
    val refresh: () -> Unit, // signal the data source to stop loading, and notify its callback
    val retry: () -> Unit,  // remake the call
    val clearCoroutineJobs: () -> Unit // the way to stop jobs from running since no lifecycle provided
)

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status: Status, val msg: String) {

    companion object {
        @JvmField
        val LOADED: NetworkState

        @JvmField
        val LOADING: NetworkState

        @JvmField
        val ERROR: NetworkState

        @JvmField
        val ENDOFLIST: NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS, "Success")

            LOADING = NetworkState(Status.RUNNING, "Running")

            ERROR = NetworkState(Status.FAILED, "Something went wrong")

            ENDOFLIST = NetworkState(Status.SUCCESS, "You have reached the end")
        }

//        fun error(msg: String) = NetworkState(
//            Status.FAILED,
//            msg
//        )
    }
}