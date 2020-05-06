package com.hidero.test.ui.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class MySMSBroadcastReceiver : BroadcastReceiver() {
    private var otpReceiver: OTPReceiveListener? = null

    fun initOTPListener(receiver: OTPReceiveListener) {
        this.otpReceiver = receiver
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val messageIntent =
                        extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                    otpReceiver?.let {
                        messageIntent?.let {messageIntent ->
                            it.onOTPReceived(messageIntent)
                        }
                    }
                }

                CommonStatusCodes.TIMEOUT ->
                    otpReceiver?.onOTPTimeOut()
            }
        }
    }


    interface OTPReceiveListener {

        fun onOTPReceived(intent: Intent?)

        fun onOTPTimeOut()
    }
}