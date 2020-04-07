package com.hidero.test.ui.fragments


import android.view.View
import androidx.fragment.app.Fragment
import com.hidero.test.R
import com.hidero.test.databinding.FragmentMessageBinding
import com.hidero.test.ui.adapters.ViewPagerAdapter
import com.hidero.test.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_message.*

/**
 * A simple [Fragment] subclass.
 */
class MessageFragment : BaseFragment<FragmentMessageBinding>() {
    private val viewPagerAdapter by lazy {
        ViewPagerAdapter(childFragmentManager)
    }

    override fun getLayoutId() = R.layout.fragment_message


    override fun initViews(view: View) {
        setHasOptionsMenu(true)
        binding.viewPager.adapter = viewPagerAdapter
        binding.tabLayout.setupWithViewPager(viewPager)
    }
}
