package com.hidero.test.ui.fragments

import android.view.View
import androidx.fragment.app.Fragment
import com.hidero.test.R
import com.hidero.test.databinding.FragmentChatsBinding
import com.hidero.test.ui.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class ChatsFragment : BaseFragment<FragmentChatsBinding>() {

    override fun getLayoutId() = R.layout.fragment_chats

    override fun initViews(view: View) {

    }

}
