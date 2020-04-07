package com.hidero.test.ui.fragments

import android.view.View
import androidx.fragment.app.Fragment
import com.hidero.test.R
import com.hidero.test.databinding.FragmentUsersBinding
import com.hidero.test.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class UsersFragment : BaseFragment<FragmentUsersBinding>() {
    override fun getLayoutId() = R.layout.fragment_users

    override fun initViews(view: View) {

    }


}
