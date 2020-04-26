package com.hidero.test.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hidero.test.R
import com.hidero.test.data.valueobject.User
import com.hidero.test.databinding.FragmentUsersBinding
import com.hidero.test.ui.adapters.OnItemClickListener
import com.hidero.test.ui.adapters.UserAdapter
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.util.RECEIVERID
import com.hidero.test.util.SEARCH
import com.hidero.test.util.USERS
import timber.log.Timber
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class UsersFragment : BaseFragment<FragmentUsersBinding>() {
    private lateinit var userAdapter: UserAdapter
    private lateinit var mUsers: MutableList<User>

    override fun getLayoutId() = R.layout.fragment_users

    override fun initViews(view: View) {
        mUsers = ArrayList()
        userAdapter = UserAdapter(requireContext(), mUsers, false)
        binding.recyclerView.adapter = userAdapter
        if (FirebaseAuth.getInstance().currentUser != null) {
            readUsers()
        }
        userAdapter.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val bundle = Bundle()
                bundle.putString(RECEIVERID, userAdapter.getItem(position).id)
                findNavController().navigate(R.id.action_messageFragment_to_chatFragment, bundle)
            }

        }
        binding.etSearchUser.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                if(FirebaseAuth.getInstance().currentUser != null){
                    searchUsers(charSequence.toString().toLowerCase(Locale.getDefault()))
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    private fun searchUsers(s: String) {
        val fuser = FirebaseAuth.getInstance().currentUser
        val query =
            FirebaseDatabase.getInstance().getReference(USERS).orderByChild(SEARCH)
                .startAt(s)
                .endAt(s + "\uf8ff")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                mUsers.clear()
                for (snapshot in dataSnapshot.children) {
                    val user: User = snapshot.getValue(User::class.java)!!
                    assert(fuser != null)
                    if (user.id != fuser!!.uid) {
                        mUsers.add(user)
                    }
                }
                userAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

    private fun readUsers() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference(USERS)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                if (TextUtils.isEmpty(binding.etSearchUser.text.toString())) {
                    mUsers.clear()
                    userAdapter.notifyDataSetChanged()

                    for (snapshot in dataSnapshot.children) {
                        val user = snapshot.getValue(User::class.java)!!
                        if (user.id != firebaseUser!!.uid) {
                            mUsers.add(user)
                        }
                    }
                    userAdapter.notifyDataSetChanged()


                }
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

}
