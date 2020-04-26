package com.hidero.test.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.hidero.test.R
import com.hidero.test.data.valueobject.FriendlyMessage
import com.hidero.test.data.valueobject.User
import com.hidero.test.util.*
import timber.log.Timber

class MessageAdapter(
    private val mContext: Context,
    private val mFriendlyMessage: MutableList<FriendlyMessage>,
    var photoUrl: String,
    options: FirebaseRecyclerOptions<FriendlyMessage>
) : FirebaseRecyclerAdapter<FriendlyMessage, MessageAdapter.ViewHolder>(options) {

    var firebaseUser: FirebaseUser? = null

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == MSG_TYPE_RIGHT) {
            val view: View =
                LayoutInflater.from(mContext).inflate(R.layout.item_chat_right, parent, false)
            ViewHolder(view)
        } else {
            val view: View =
                LayoutInflater.from(mContext).inflate(R.layout.item_chat_left, parent, false)
            ViewHolder(view)
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val friendlyMessage = mFriendlyMessage[position]
        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (getItemViewType(position) == MSG_TYPE_LEFT) {
            if (photoUrl == DEFAULT) {
                viewHolder.profile_image.setImageResource(R.drawable.ic_user_profile)
            } else {
                Glide.with(mContext).load(photoUrl).into(viewHolder.profile_image)
            }
        } else {
            val reference =
                FirebaseDatabase.getInstance().getReference(USERS).child(firebaseUser!!.uid)
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user != null) {
                        if (user.photoUrl.equals(DEFAULT)) {
                            viewHolder.profile_image.setImageResource(R.drawable.ic_user_profile)
                        } else {
                            if (mContext.isAvailable()) {
                                Glide.with(mContext).load(user.photoUrl)
                                    .into(viewHolder.profile_image)
                            }
                        }
                    }

                }

                override fun onCancelled(@NonNull databaseError: DatabaseError) {}
            })
        }

        if (position == mFriendlyMessage.size - 1) {
            if (friendlyMessage.seen) {
                viewHolder.txt_seen.text = mContext.resources.getString(R.string.seen)
            } else {
                viewHolder.txt_seen.text = mContext.resources.getString(R.string.delivered)
            }
        } else {
            viewHolder.txt_seen.visibility = View.GONE
        }

        if (friendlyMessage.message != null) {
            viewHolder.messageTextView.text = friendlyMessage.message
            viewHolder.messageTextView.visibility = TextView.VISIBLE
            viewHolder.messageImageView.visibility = ImageView.GONE
        } else if (friendlyMessage.imageUrl != null) {
            val imageUrl = friendlyMessage.imageUrl!!
            if (imageUrl.startsWith("gs://")) {
                val storageReference = FirebaseStorage.getInstance()
                    .getReferenceFromUrl(imageUrl)
                storageReference.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl =
                            task.result.toString()
                        Glide.with(viewHolder.messageImageView.context)
                            .load(downloadUrl)
                            .into(viewHolder.messageImageView)
                    } else {
                        Timber.e(task.exception)
                    }
                }
            } else {
                Glide.with(viewHolder.messageImageView.context)
                    .load(friendlyMessage.imageUrl)
                    .into(viewHolder.messageImageView)
            }
            viewHolder.messageImageView.visibility = ImageView.VISIBLE
            viewHolder.messageTextView.visibility = TextView.GONE
        }


    }

    override fun getItemCount(): Int {
        return mFriendlyMessage.size
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        var profile_image: ImageView = itemView.findViewById(R.id.ivProfile)
        var txt_seen: TextView = itemView.findViewById(R.id.txt_seen)
        var messageImageView: ImageView = itemView.findViewById(R.id.messageImageView)

    }

    override fun getItemViewType(position: Int): Int {
        return if (mFriendlyMessage[position].sender == firebaseUser?.uid) {
            MSG_TYPE_RIGHT
        } else {
            MSG_TYPE_LEFT
        }
    }

    companion object {

    }

    fun setItems(newArticles: MutableList<FriendlyMessage>) {
        //get the current items
        val currentSize: Int = mFriendlyMessage.size
        //remove the current items
        mFriendlyMessage.clear()
        //add all the new items
        mFriendlyMessage.addAll(newArticles)
        //tell the recycler view that all the old items are gone
        notifyItemRangeRemoved(0, currentSize)
        //tell the recycler view how many new items we added
        notifyItemRangeInserted(0, newArticles.size)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: FriendlyMessage) {

    }

}
