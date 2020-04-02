package com.hidero.test.ui.fragments


import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hidero.test.R
import com.hidero.test.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }

    override fun initViews(view: View) {
        setHasOptionsMenu(true)
        toolBar.inflateMenu(R.menu.profile_menu)
        toolBar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_home -> {

            }
            R.id.menu_message -> {

            }
            R.id.menu_account -> {

            }
            R.id.menu_help -> {

            }
            R.id.menu_feed_back -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

}
