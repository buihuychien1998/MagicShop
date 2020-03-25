package com.hidero.test.ui.adapters

import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(manager: FragmentManager) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragments: ArrayList<Fragment>
    private val titles: ArrayList<String>
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }

    // Ctrl + O
    @Nullable
    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }

    init {
        fragments = ArrayList()
        titles = ArrayList()
    }
}