package com.hidero.test.ui.fragments


import android.view.View
import androidx.fragment.app.Fragment
import com.hidero.test.R
import com.hidero.test.databinding.FragmentChangePasswordBinding
import com.hidero.test.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>() {

    override fun getLayoutId() = R.layout.fragment_change_password

    override fun initViews(view: View) {
        binding.toolBar.inflateMenu(R.menu.profile_menu)
        binding.toolBar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
    }

}
