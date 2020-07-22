package com.hidero.test.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hidero.test.ui.notifications.Token
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.hidero.test.R
import com.hidero.test.data.valueobject.ChatList
import com.hidero.test.data.valueobject.User
import com.hidero.test.databinding.FragmentChatsBinding
import com.hidero.test.ui.adapters.OnItemClickListener
import com.hidero.test.ui.adapters.UserAdapter
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.util.CHAT_LIST
import com.hidero.test.util.RECEIVERID
import com.hidero.test.util.TOKENS
import com.hidero.test.util.USERS
import timber.log.Timber
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ChatsFragment : BaseFragment<FragmentChatsBinding>() {
    private lateinit var userAdapter: UserAdapter
    private lateinit var mUsers: MutableList<User>
    private var fuser: FirebaseUser? = null
    private lateinit var reference: DatabaseReference
    private lateinit var usersList: MutableList<ChatList>

    override fun getLayoutId() = R.layout.fragment_chats

    override fun initViews(view: View) {
        fuser = FirebaseAuth.getInstance().currentUser
        mUsers = ArrayList()
        userAdapter = UserAdapter(requireContext(), mUsers, true)
        binding.recyclerView.adapter = userAdapter
        userAdapter.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val bundle = Bundle()
                bundle.putString(RECEIVERID, userAdapter.getItem(position).id)
//                findNavController().navigate(R.id.action_messageFragment_to_chatFragment, bundle)
            }

        }
        usersList = ArrayList()
        fuser?.apply {
            reference = FirebaseDatabase.getInstance().getReference(CHAT_LIST).child(fuser?.uid!!)
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    usersList.clear()
                    for (snapshot in dataSnapshot.children) {
                        snapshot.getValue(ChatList::class.java)?.let {
                            usersList.add(it)
                        }
                    }
                    chatList()
                }

                override fun onCancelled(@NonNull databaseError: DatabaseError) {}
            })
        }

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (it.isSuccessful) {
                try {
                    updateToken(it.result?.token)
                } catch (ex: Exception) {
                    Timber.e(ex)
                }
            }

        }
    }


    private fun updateToken(token: String?) {
        val reference = FirebaseDatabase.getInstance().getReference(TOKENS)
        val token1 = Token(token)
        reference.child(fuser!!.uid).setValue(token1)
    }

    private fun chatList() {
        reference = FirebaseDatabase.getInstance().getReference(USERS)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                mUsers.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)!!
                    for (chatList in usersList) {
                        if (user.id == chatList.id) {
                            mUsers.add(user)
                        }
                    }
                }
                userAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }
}
