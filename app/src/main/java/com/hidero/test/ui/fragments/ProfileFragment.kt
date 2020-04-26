package com.hidero.test.ui.fragments


import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hidero.test.R
import com.hidero.test.databinding.FragmentProfileBinding
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.EventObserver
import com.hidero.test.ui.viewmodels.SettingViewModel

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private lateinit var viewModel: SettingViewModel
    override fun getLayoutId() = R.layout.fragment_profile

    override fun initViews(view: View) {
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this)[SettingViewModel::class.java]
        binding.handlers = viewModel
        binding.toolBar.inflateMenu(R.menu.profile_menu)
        binding.toolBar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        viewModel.navigateTo.observe(viewLifecycleOwner, EventObserver{
            handleEvent(it)
        })

    }

    private fun handleEvent(view: View) {
        when (view.id) {
            R.id.btnBack -> {
                findNavController().navigateUp()
            }
            R.id.rl2->{
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToChangePasswordFragment())
            }

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
