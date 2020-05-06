package com.hidero.test.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mychatapp.notifications.Sender
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hidero.test.R
import com.hidero.test.data.valueobject.FriendlyMessage
import com.hidero.test.data.valueobject.User
import com.hidero.test.databinding.FragmentChatBinding
import com.hidero.test.ui.adapters.MessageAdapter
import com.hidero.test.ui.adapters.WrapContentLinearLayoutManager
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.notifications.Data
import com.hidero.test.ui.notifications.Token
import com.hidero.test.ui.viewmodels.EventObserver
import com.hidero.test.ui.viewmodels.MessageViewModel
import com.hidero.test.util.*
import timber.log.Timber
import java.util.*

class ChatFragment : BaseFragment<FragmentChatBinding>() {
    private lateinit var viewModel: MessageViewModel
    private lateinit var mFirebaseUser: FirebaseUser
    private lateinit var reference: DatabaseReference
    lateinit var messageAdapter: MessageAdapter
    lateinit var mchat: MutableList<FriendlyMessage>
    var seenListener: ValueEventListener? = null
    private lateinit var receiverId: String
    var notify = false
    lateinit var options: FirebaseRecyclerOptions<FriendlyMessage>
    private lateinit var mFirebaseDatabaseReference: DatabaseReference
    private var mLinearLayoutManager: LinearLayoutManager? = null
    override fun getLayoutId() = R.layout.fragment_chat

    override fun initViews(view: View) {
        viewModel = ViewModelProvider(this)[MessageViewModel::class.java]
        binding.handlers = viewModel
        viewModel.navigateTo.observe(viewLifecycleOwner, EventObserver {
            handleEvent(it)
        })
        mLinearLayoutManager =
            WrapContentLinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mLinearLayoutManager?.stackFromEnd = true
        binding.recyclerView.layoutManager = mLinearLayoutManager

        receiverId = arguments?.getString(RECEIVERID).toString()
        mFirebaseUser = FirebaseAuth.getInstance().currentUser!!

        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference
        val parser: SnapshotParser<FriendlyMessage> =
            SnapshotParser { dataSnapshot ->
                val friendlyMessage = dataSnapshot.getValue(
                    FriendlyMessage::class.java
                )!!
                friendlyMessage.id = dataSnapshot.key
                friendlyMessage
            }
        val messagesRef = mFirebaseDatabaseReference.child(CHATS)
        options =
            FirebaseRecyclerOptions.Builder<FriendlyMessage>()
                .setQuery(messagesRef, parser)
                .build()
        mchat = ArrayList()
        messageAdapter = MessageAdapter(requireContext(), mchat, DEFAULT, options)
        messageAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                val friendlyMessageCount = messageAdapter.itemCount
                binding.recyclerView.scrollToPosition(friendlyMessageCount - 1)
//                val lastVisiblePosition =
//                    mLinearLayoutManager?.findLastCompletelyVisibleItemPosition()
//                if (lastVisiblePosition == -1 ||
//                    positionStart >= friendlyMessageCount - 1 &&
//                    lastVisiblePosition == positionStart - 1) {
//                    binding.recyclerView.smoothScrollToPosition(positionStart)
//                }
            }
        })
        binding.recyclerView.apply {
            adapter = messageAdapter
            smoothScrollToPositionWithSpeed(messageAdapter.itemCount)
            addOnScrollListener(
                OscillatingScrollListener(resources.getDimensionPixelSize(R.dimen.dp16))
            )
        }
        reference = mFirebaseDatabaseReference.child(USERS).child(receiverId)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)!!
                messageAdapter.photoUrl = user.photoUrl
                readMessage(mFirebaseUser.uid, receiverId)
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })

        seenMessage(receiverId)
    }

    private fun handleEvent(view: View) {
        when (view.id) {
            R.id.btnBack -> findNavController().navigateUp()

            R.id.btnSend -> {
                notify = true
                val msg: String = binding.edtSend.text.toString()
                if (!TextUtils.isEmpty(msg)) {
                    sendMessage(mFirebaseUser.uid, receiverId, msg)
                } else {
                    requireActivity().showToast("You can't send empty message")
                }
                binding.edtSend.setText("")
            }
            R.id.btnAddImage -> openImage()
        }
    }

    private fun seenMessage(userId: String) {
        reference = FirebaseDatabase.getInstance().getReference(CHATS)
        seenListener = reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val chat = snapshot.getValue(FriendlyMessage::class.java)!!
                    if (chat.receiver == mFirebaseUser.uid && chat.sender == userId) {
                        val hashMap =
                            HashMap<String, Any>()
                        hashMap[SEEN] = true
                        snapshot.ref.updateChildren(hashMap)
                    }
                }
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

    private fun sendMessage(
        sender: String,
        receiver: String,
        message: String
    ) {
        var reference = FirebaseDatabase.getInstance().reference
        val hashMap =
            HashMap<String, Any>()
        hashMap[SENDER] = sender
        hashMap[RECEIVER] = receiver
        hashMap[MESSAGE] = message
        hashMap[SEEN] = false
        reference.child(CHATS).push().setValue(hashMap)
        // add user to chat fragment
        val chatRef = FirebaseDatabase.getInstance().getReference(CHAT_LIST)
            .child(mFirebaseUser.uid)
            .child(receiverId)
        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child(ID).setValue(receiverId)
                }
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
        val chatRefReceiver =
            FirebaseDatabase.getInstance().getReference(CHAT_LIST)
                .child(receiverId)
                .child(mFirebaseUser.uid)
        chatRefReceiver.child(ID).setValue(mFirebaseUser.uid)
        reference = FirebaseDatabase.getInstance().getReference(USERS).child(mFirebaseUser.uid)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)!!
                if (notify) {
                    sendNotifyAction(receiver, user.username, message)
                }
                notify = false
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

    private fun sendNotifyAction(
        receiver: String,
        username: String,
        message: String
    ) {
        val tokens = FirebaseDatabase.getInstance().getReference(TOKENS)
        val query = tokens.orderByKey().equalTo(receiver)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val token = snapshot.getValue(Token::class.java)
                    val data = Data(
                        mFirebaseUser.uid,
                        R.drawable.ic_user_profile,
                        "$username: $message",
                        "New Message",
                        receiverId
                    )
                    val sender = Sender(data, token?.token)
                    viewModel.sendNotification(sender)
                }
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

    private fun readMessage(
        myId: String,
        userId: String
    ) {
        reference = FirebaseDatabase.getInstance().getReference(CHATS)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                val tmpList = ArrayList<FriendlyMessage>()
                for (snapshot in dataSnapshot.children) {
                    val chat = snapshot.getValue(FriendlyMessage::class.java)!!
                    if (chat.receiver.equals(myId) && chat.sender.equals(userId) ||
                        chat.receiver.equals(userId) && chat.sender.equals(myId)
                    ) {
                        tmpList.add(chat)
                    }

                }
                messageAdapter.setItems(tmpList)
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }

    private fun currentUser(userId: String) {
        val editor =
            requireActivity().getSharedPreferences("PREFS", AppCompatActivity.MODE_PRIVATE).edit()
        editor.putString(CURRENTUSER, userId)
        editor.apply()
    }

    private fun status(status: String) {
        reference = FirebaseDatabase.getInstance().getReference(USERS).child(mFirebaseUser.uid)
        val hashMap =
            HashMap<String, Any>()
        hashMap[STATUS] = status
        reference.updateChildren(hashMap)
    }

    override fun onResume() {
        super.onResume()
        status(ONLINE)
        currentUser(receiverId)
        messageAdapter.startListening()
    }

    override fun onPause() {
        messageAdapter.stopListening()
        super.onPause()
        reference.removeEventListener(seenListener!!)
        status(OFFLINE)
        currentUser(NONE)

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val uri = data.data
                    val tempMessage = FriendlyMessage(
                        mFirebaseUser.uid, receiverId, null,
                        LOADING_IMAGE_URL, false
                    )
                    mFirebaseDatabaseReference.child(CHATS).push()
                        .setValue(
                            tempMessage
                        ) { databaseError, databaseReference ->
                            if (databaseError == null) {
                                val key = databaseReference.key
                                val storageReference =
                                    FirebaseStorage.getInstance()
                                        .getReference(mFirebaseUser.uid)
                                        .child(key!!)
                                        .child(uri!!.lastPathSegment!!)
                                putImageInStorage(storageReference, uri, key)
                            } else {
                                Timber.e(databaseError.toException())
                            }
                        }
                }
            }
        }
    }


    private fun putImageInStorage(storageReference: StorageReference, uri: Uri, key: String) {
        storageReference.putFile(uri).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                task.result?.metadata?.reference?.downloadUrl?.apply {
                    addOnCompleteListener(
                        requireActivity()
                    ) { t ->
                        val friendlyMessage = FriendlyMessage(
                            mFirebaseUser.uid,
                            receiverId,
                            null,
                            t.result.toString(),
                            false
                        )
                        Timber.e("Getting download url was successful. ${t.result}")
                        mFirebaseDatabaseReference.child(CHATS).child(key)
                            .setValue(friendlyMessage)
                        logMessageSent()
                    }
                }

            } else {
                Timber.e(task.exception)
            }
        }
    }

    private fun logMessageSent() {}

}
