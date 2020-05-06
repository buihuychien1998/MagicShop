package com.hidero.test

import android.app.Application
import com.google.gson.Gson
import com.hidero.test.ui.AppSignatureHelper
import timber.log.Timber


class App : Application() {
    var gSon: Gson? = null
    override fun onCreate() {
        super.onCreate()
        mSelf = this
        gSon = Gson()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        val appSignature = AppSignatureHelper(this)
        appSignature.appSignatures
    }

    companion object {
        private var mSelf: App? = null
        fun self(): App? {
            return mSelf
        }
    }
}