package com.hidero.test.ui.fragments


import android.view.View
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.hidero.test.R
import com.hidero.test.data.valueobject.FriendlyMessage
import com.hidero.test.data.valueobject.User
import com.hidero.test.databinding.FragmentMessageBinding
import com.hidero.test.ui.adapters.ViewPagerAdapter
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.MessageViewModel
import com.hidero.test.util.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class MessageFragment : BaseFragment<FragmentMessageBinding>() {
    private val viewPagerAdapter by lazy {
        ViewPagerAdapter(childFragmentManager)
    }
    private var firebaseUser: FirebaseUser? = null
    private lateinit var reference: DatabaseReference
    private val viewModel by lazy {
        ViewModelProvider(this)[MessageViewModel::class.java]
    }

    override fun getLayoutId() = R.layout.fragment_message


    override fun initViews(view: View) {
        binding.handlers = viewModel

        viewModel.refreshAccount()
        firebaseUser = FirebaseAuth.getInstance().currentUser?.apply {
            reference =
                FirebaseDatabase.getInstance().getReference(USERS).child(uid)
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(User::class.java)
                    viewModel.refreshUser(user)
                }

                override fun onCancelled(@NonNull databaseError: DatabaseError) {}
            })
            reference = FirebaseDatabase.getInstance().getReference(CHATS)
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    var unread = 0
                    for (snapshot in dataSnapshot.children) {
                        snapshot.getValue(FriendlyMessage::class.java)?.let {
                            if (it.receiver.equals(uid) && !it.seen) {
                                unread++
                            }
                        }

                    }
                    viewPagerAdapter.clear()
                    viewPagerAdapter.notifyDataSetChanged()
                    if (unread == 0) {
                        viewPagerAdapter.addFragment(ChatsFragment(), "Tin nhắn")
                    } else {
                        viewPagerAdapter.addFragment(ChatsFragment(), "($unread) Tin nhắn")
                    }
                    viewPagerAdapter.addFragment(UsersFragment(), "Người dùng khác")
                    binding.viewPager.adapter = viewPagerAdapter
                    binding.tabLayout.setupWithViewPager(binding.viewPager)

                }

                override fun onCancelled(@NonNull databaseError: DatabaseError) {}
            })
        }


    }

    private fun status(status: String) {
        firebaseUser?.let {
            reference =
                FirebaseDatabase.getInstance().getReference(USERS).child(it.uid)
            val hashMap =
                HashMap<String, Any>()
            hashMap[STATUS] = status
            reference.updateChildren(hashMap)
        }

    }

    override fun onResume() {
        super.onResume()
        binding.tabLayout.getTabAt(0)?.select()
        binding.viewPager.post {
            binding.viewPager.currentItem = 0
        }
        binding.tabLayout.setScrollPosition(0, 0f, true)
        status(ONLINE)
    }

    override fun onPause() {
        super.onPause()
        status(OFFLINE)
    }
}
