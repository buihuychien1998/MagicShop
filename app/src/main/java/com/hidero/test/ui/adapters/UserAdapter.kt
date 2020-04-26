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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hidero.test.R
import com.hidero.test.data.valueobject.FriendlyMessage
import com.hidero.test.data.valueobject.User
import com.hidero.test.util.CHATS
import com.hidero.test.util.DEFAULT
import com.hidero.test.util.ONLINE

class UserAdapter(
    private val mContext: Context,
    private val mUsers: List<User>,
    private val isChat: Boolean
) :
    RecyclerView.Adapter<UserAdapter.ViewHolder?>() {
    var onItemClickListener: OnItemClickListener? = null
    var theLastMessage: String? = null

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false))


    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        val user: User = mUsers[position]
        holder.username.setText(user.username)
        if (user.photoUrl.equals(DEFAULT)) {
            holder.profile_image.setImageResource(R.drawable.ic_user_profile)
        } else {
            Glide.with(mContext).load(user.photoUrl).into(holder.profile_image)
        }
        if (isChat) {
            lastMessage(user.id, holder.last_msg)
        } else {
            holder.last_msg.visibility = View.GONE
        }
        if (isChat) {
            if (user.status.equals(ONLINE)) {
                holder.img_on.visibility = View.VISIBLE
                holder.img_off.visibility = View.GONE
            } else {
                holder.img_on.visibility = View.GONE
                holder.img_off.visibility = View.VISIBLE
            }
        } else {
            holder.img_on.visibility = View.GONE
            holder.img_off.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            //navigate chat fragment
            onItemClickListener?.onItemClick(holder.adapterPosition)
        }
    }

    fun getItem(position: Int) = mUsers[position]
    override fun getItemCount() = mUsers.size


    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var username: TextView
        var profile_image: ImageView
        val img_on: ImageView
        val img_off: ImageView
        val last_msg: TextView

        init {
            username = itemView.findViewById(R.id.tvUsername)
            profile_image = itemView.findViewById(R.id.ivProfile)
            img_on = itemView.findViewById(R.id.img_on)
            img_off = itemView.findViewById(R.id.img_off)
            last_msg = itemView.findViewById(R.id.last_msg)
        }
    }

    //check for last message
    private fun lastMessage(userId: String, last_msg: TextView) {
        theLastMessage = DEFAULT
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference(CHATS)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val friendlyMessage = snapshot.getValue(FriendlyMessage::class.java)
                    if (firebaseUser != null && friendlyMessage != null) {
                        if (friendlyMessage.receiver.equals(firebaseUser.uid) && friendlyMessage.sender.equals(
                                userId
                            ) ||
                            friendlyMessage.receiver.equals(userId) && friendlyMessage.sender.equals(
                                firebaseUser.uid
                            )
                        ) {
                            theLastMessage = friendlyMessage.message
                        }
                    }
                }
                when (theLastMessage) {
                    DEFAULT -> last_msg.text = "No Message"
                    else -> last_msg.text = theLastMessage
                }
                theLastMessage = DEFAULT
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
        })
    }
}