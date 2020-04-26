package com.hidero.test.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class AuthenticationPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragmentList: ArrayList<Fragment> = ArrayList()
    override fun getItem(i: Int) = fragmentList[i]

    override fun getCount() = fragmentList.size

    val addFragment = { fragment: Fragment ->
        fragmentList.add(fragment)
    }
}