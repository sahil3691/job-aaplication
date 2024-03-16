package com.example.jobapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jobapplication.R
import com.example.jobapplication.models.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ChatAdapter(
    private val context: Context,
    private val mChats: ArrayList<Chat>,
    private val imageURL: String
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    companion object {
        const val MSG_LEFT = 0
        const val MSG_RIGHT = 1
    }

    private var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == MSG_RIGHT) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false)
            ViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false)
            ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = mChats[position]
        holder.showMessage.text = chat.message

        if (imageURL == "default") {
            holder.profile.setImageResource(R.drawable.user)
        } else {
            Glide.with(context).load(imageURL).into(holder.profile)
        }

        if (position == mChats.size - 1) {
            if (chat.isSeen) {
                holder.seenText.text = "Seen"
            } else {
                holder.seenText.text = "Delivered"
            }
        } else {
            holder.seenText.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mChats.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val showMessage: TextView = itemView.findViewById(R.id.show_message)
        val profile: ImageView = itemView.findViewById(R.id.Profile_image)
        val seenText: TextView = itemView.findViewById(R.id.txt_seen_status)
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        return if (mChats[position].sender == firebaseUser?.uid) {
            MSG_RIGHT
        } else {
            MSG_LEFT
        }
    }
}
