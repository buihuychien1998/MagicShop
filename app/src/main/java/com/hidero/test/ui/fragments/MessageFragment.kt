package com.hidero.test.ui.fragments


import android.view.View
import androidx.fragment.app.Fragment
import com.hidero.test.R
import com.hidero.test.ui.adapters.ViewPagerAdapter
import com.hidero.test.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_message.*

/**
 * A simple [Fragment] subclass.
 */
class MessageFragment : BaseFragment() {
    private val viewPagerAdapter by lazy {
        ViewPagerAdapter(requireActivity().supportFragmentManager)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_message
    }

    override fun initViews(view: View) {
        setHasOptionsMenu(true)
        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
}
