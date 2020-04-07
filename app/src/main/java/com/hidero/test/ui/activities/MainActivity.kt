package com.hidero.test.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.hidero.test.R
import com.hidero.test.databinding.ActivityMainBinding
import com.hidero.test.ui.base.BaseActivity
import com.hidero.test.ui.dialogs.InternetDialog
import com.hidero.test.util.setupWithNavController

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
            R.navigation.news,
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
            binding.bottomNav.visibility = View.VISIBLE
        } else {
            binding.bottomNav.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
