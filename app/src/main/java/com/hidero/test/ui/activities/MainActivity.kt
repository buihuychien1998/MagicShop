package com.hidero.test.ui.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.hidero.test.R
import com.hidero.test.data.valueobject.Account
import com.hidero.test.databinding.ActivityMainBinding
import com.hidero.test.ui.base.BaseActivity
import com.hidero.test.ui.dialogs.InternetDialog
import com.hidero.test.util.*
import timber.log.Timber


class MainActivity : BaseActivity<ActivityMainBinding>(), BaseActivity.OnNetworkConnectedListener {
    private var currentNavController: LiveData<NavController>? = null
    private val internetDialog by lazy {
        InternetDialog(this)
    }

    override fun onNetworkConnected() {
        if (internetDialog.isShowing) {
            internetDialog.dismiss()
        }
    }

    override fun onNetworkDisconnected() {
        internetDialog.showDialog()
    }

    override fun getLayoutId() = R.layout.activity_main


    override fun initViews(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
        setOnNetworkConnectedListener(this)
        enableStatistic()
        PermissionUtil.checkPermission(this)
    }

    fun enableStatistic() {
        if (SharedPrefs.instance[CURRENT_USER, Account::class.java] != null) {
            SharedPrefs.instance[CURRENT_USER, Account::class.java]?.role?.let {
                binding.bottomNav.menu.findItem(R.id.message).isEnabled = it == 1
            }
        } else {
            binding.bottomNav.menu.findItem(R.id.message).isEnabled = false

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) || ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) ||  ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.CALL_PHONE
                        ) ||  ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_SMS
                        ) || ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.SEND_SMS
                        )
                    ) {
                        AlertDialog.Builder(this)
                            .setMessage("Your error message here")
                            .setPositiveButton(
                                "Allow"
                            ) { dialog, which ->  ActivityCompat.requestPermissions(
                                this, PermissionUtil.PERMISSIONS
                                , REQUEST_PERMISSION

                            ) }
                            .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                            .create()
                            .show()
                    } else {
                        PermissionUtil.requestPermissionInSetting(this)
                    }
                }
            }
        }
    }


//    private val currentNavController: NavController by lazy {
//        Navigation.findNavController(this, R.id.nav_host_fragment)
//    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
//        NavigationUI.setupWithNavController(bottomNav, currentNavController)
        val navGraphIds = listOf(
            R.navigation.home,
            R.navigation.category,
            R.navigation.message,
            R.navigation.cart,
            R.navigation.account
        )
        // Setup the bottom navigation view with a list of navigation graphs
        val controller = binding.bottomNav.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )
        // Whenever the selected controller changes, setup the action bar.
//        controller.observe(this, Observer { _ ->
        //            setupActionBarWithNavController(navController)
//        })
        currentNavController = controller

    }

    override fun onSupportNavigateUp() =
//        return currentNavController.navigateUp()
        currentNavController?.value?.navigateUp() ?: false


    fun visibilityBottomNav(visible: Boolean) {
        if (visible) {
            binding.bottomNav.show()
        } else {
            binding.bottomNav.hide()
        }
    }

}
