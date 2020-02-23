package com.hidero.test.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.hidero.test.R
import com.hidero.test.base.BaseActivity
import com.hidero.test.ui.dialogs.InternetDialog
import com.hidero.test.util.showToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), BaseActivity.OnNetworkConnectedListener {
    private val internetDialog by lazy {
        InternetDialog(this)
    }
    override fun onNetworkConnected() {
        if (internetDialog.isShowing){
            internetDialog.dismiss()
        }
    }

    override fun onNetworkDisconnected() {
        internetDialog.showDialog()
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_main
    }

    override fun initViews(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
        setOnNetworkConnectedListener(this)
    }

    private val currentNavController: NavController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment)
    }

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
        NavigationUI.setupWithNavController(bottomNav, currentNavController)
    }
    override fun onSupportNavigateUp(): Boolean {
        return currentNavController.navigateUp()
    }

}
