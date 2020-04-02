package com.hidero.test.ui.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hidero.test.util.ACTION_NETWORK_CHANGE
import com.hidero.test.util.SUPPLICANT_CONNECTION_CHANGE_ACTION
import com.hidero.test.util.isNetworkAvailable

abstract class BaseActivity : AppCompatActivity() {

    var isNetworkState: Boolean = false
    abstract fun getLayoutId(): Int
    abstract fun initViews(savedInstanceState: Bundle?)

    //Listener lắng nghe sự thay đổi trạng thái connect internet
    private var onNetworkConnectedListener: OnNetworkConnectedListener? = null

    fun setOnNetworkConnectedListener(onNetworkConnectedListener: OnNetworkConnectedListener) {
        this.onNetworkConnectedListener = onNetworkConnectedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNetworkState = this.isNetworkAvailable()
        registerBroadcastReceiver()
        setContentView(getLayoutId())
        initViews(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        isNetworkState = this.isNetworkAvailable()
    }


    //Đăng ký broadcast lắng nghe sự kiện thay đổi network
    private fun registerBroadcastReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("service.to.activity.transfer")
        intentFilter.addAction(SUPPLICANT_CONNECTION_CHANGE_ACTION)
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
                        isNetworkState = this@BaseActivity.isNetworkAvailable()
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

    interface OnNetworkConnectedListener {
        fun onNetworkConnected()
        fun onNetworkDisconnected()
    }

}