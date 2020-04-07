package com.hidero.test

import android.app.Application
import com.google.gson.Gson
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
    }

    companion object {
        private var mSelf: App? = null
        fun self(): App? {
            return mSelf
        }
    }
}