package com.hidero.test.ui.fragments


import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.hidero.test.R
import com.hidero.test.databinding.FragmentSettingBinding
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.EventObserver
import com.hidero.test.ui.viewmodels.SettingViewModel
import com.hidero.test.util.CURRENT_USER
import com.hidero.test.util.SharedPrefs

/**
 * A simple [Fragment] subclass.
 */
class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override fun getLayoutId() = R.layout.fragment_setting
    private lateinit var viewModel: SettingViewModel

    override fun initViews(view: View) {
        viewModel = ViewModelProvider(this)[SettingViewModel::class.java]
        binding.handlers = viewModel
        viewModel.navigateTo.observe(viewLifecycleOwner, EventObserver {
            handleEvent(it)
        })
    }

    private fun handleEvent(view: View) {
        when (view.id) {
            R.id.btnBack -> {
                findNavController().navigateUp()
            }
            R.id.btnInfoAccount -> {
                findNavController().navigate(SettingFragmentDirections.actionSettingFragmentToProfileFragment())
            }

            R.id.btnLogout -> {
                FirebaseAuth.getInstance().signOut()
                SharedPrefs.instance.put(CURRENT_USER, null)
                viewModel.refreshAccount()
                findNavController().navigateUp()
            }
        }

    }

}
