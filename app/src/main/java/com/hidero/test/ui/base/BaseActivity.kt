package com.hidero.test.ui.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.hidero.test.util.ACTION_NETWORK_CHANGE
import com.hidero.test.util.SUPPLICANT_CONNECTION_CHANGE_ACTION
import com.hidero.test.util.isNetworkAvailable

abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity() {
    var isNetworkState: Boolean = false
    lateinit var binding: DB
    abstract fun getLayoutId(): Int
    abstract fun initViews(savedInstanceState: Bundle?)

    //Listener lắng nghe sự thay đổi trạng thái connect internet
    private var onNetworkConnectedListener: OnNetworkConnectedListener? = null

    fun setOnNetworkConnectedListener(onNetworkConnectedListener: OnNetworkConnectedListener) {
        this.onNetworkConnectedListener = onNetworkConnectedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding.lifecycleOwner = this
        registerBroadcastReceiver()
        isNetworkState = isNetworkAvailable()
        initViews(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        isNetworkState = isNetworkAvailable()
    }


    //Đăng ký broadcast lắng nghe sự kiện thay đổi network
    private fun registerBroadcastReceiver() {
        IntentFilter().apply {
            addAction("service.to.activity.transfer")
            addAction(SUPPLICANT_CONNECTION_CHANGE_ACTION)
            addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
            addAction(ACTION_NETWORK_CHANGE)
            registerReceiver(receiver, this)
        }
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