package com.hidero.test.ui.adapters

import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.hidero.test.ui.fragments.ChatsFragment
import com.hidero.test.ui.fragments.UsersFragment

class ViewPagerAdapter(manager: FragmentManager) :
    FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragments: ArrayList<Fragment> = arrayListOf(ChatsFragment(), UsersFragment())
    private val titles: ArrayList<String> = arrayListOf("Tin nhắn", "Người dùng")
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

}