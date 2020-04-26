package com.hidero.test.ui.adapters

import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(manager: FragmentManager) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    //    private val fragments: ArrayList<Fragment> = arrayListOf(ChatsFragment(), UsersFragment())
    private var fragments: ArrayList<Fragment> = arrayListOf()

    //    private val titles: ArrayList<String> = arrayListOf("Tin nhắn", "Người dùng")
    private val titles: ArrayList<String> = arrayListOf()
    override fun getItem(position: Int) = fragments[position]


    override fun getCount() = fragments.size


    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }

    val clear = {
        fragments.clear()
        titles.clear()
    }

    // Ctrl + O
    @Nullable
    override fun getPageTitle(position: Int) = titles[position]


    override fun saveState() = null

}