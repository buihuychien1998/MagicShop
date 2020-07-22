package com.hidero.test.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat


object PermissionUtil {
    val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_SMS,
        Manifest.permission.SEND_SMS
    )

    fun hasPermissions(
        context: Context?,
        permissions: Array<String>
    ): Boolean {
        if (context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    fun checkPermission(mActivity: Activity): Boolean {
        if (!hasPermissions(mActivity, PERMISSIONS)) {

            // Do something, when permissions not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) ||  ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity,
                    Manifest.permission.CALL_PHONE
                ) ||  ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity,
                    Manifest.permission.READ_SMS
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity,
                    Manifest.permission.SEND_SMS
                )
            ) {
                ActivityCompat.requestPermissions(
                    mActivity, PERMISSIONS,
                    REQUEST_PERMISSION
                )
            } else {
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                    mActivity, PERMISSIONS,
                    REQUEST_PERMISSION
                )

            }

            return false
        } else {
            // Do something, when permissions are already granted
            return true
        }
    }

    fun requestPermissionInSetting(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivity(intent)

    }
}
