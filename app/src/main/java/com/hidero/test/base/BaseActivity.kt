package com.hidero.test.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hidero.test.util.ACTION_NETWORK_CHANGE
import com.hidero.test.util.getInternetStatus

abstract class BaseActivity : AppCompatActivity() {

    var isNetworkState: Boolean = false

    abstract fun getLayoutID(): Int
    abstract fun initViews(savedInstanceState: Bundle?)

    //Listener lắng nghe sự thay đổi trạng thái connect internet
    private var onNetworkConnectedListener: OnNetworkConnectedListener? = null
    fun setOnNetworkConnectedListener(onNetworkConnectedListener: OnNetworkConnectedListener) {
        this.onNetworkConnectedListener = onNetworkConnectedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNetworkState = this.getInternetStatus()
        registerBroadcastReciver()
        setContentView(getLayoutID())
        initViews(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        isNetworkState = this.getInternetStatus()
    }


    //Đăng ký broadcast lắng nghe sự kiện thay đổi network
    private fun registerBroadcastReciver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("service.to.activity.transfer")
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        intentFilter.addAction(ACTION_NETWORK_CHANGE)
        registerReceiver(receiver, intentFilter)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action != null) {
                when (action) {
                    ACTION_NETWORK_CHANGE -> {
                        isNetworkState = this@BaseActivity.getInternetStatus()
                        if (isNetworkState) {
                            onNetworkConnectedListener.let {
                                it?.onNetworkConnected()
                            }
                        } else {
                            onNetworkConnectedListener.let {
                                it?.onNetworkDisconnected()
                            }
                        }
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    interface OnNetworkConnectedListener{
        fun onNetworkConnected()
        fun onNetworkDisconnected()
    }
}