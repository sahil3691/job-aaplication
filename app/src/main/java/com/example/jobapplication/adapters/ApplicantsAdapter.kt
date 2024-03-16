package com.example.jobapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jobapplication.R
import com.example.jobapplication.activities.ApplicantDetailActivity
import com.example.jobapplication.models.User

import de.hdodenhof.circleimageview.CircleImageView

class ApplicantsAdapter(private val context: Context, private val userList: List<User>, private val jobId : String) :
    RecyclerView.Adapter<ApplicantsAdapter.ApplicantViewHolder>() {

    class ApplicantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.textView)
        val userImageView: CircleImageView = itemView.findViewById(R.id.imageViewSearch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicantViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ApplicantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ApplicantViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.usernameTextView.text = currentUser.username
        // Load user image using Picasso (or any other image loading library)
        Glide.with(context)
            .load(currentUser.image)
            .into(holder.userImageView)

        // Set click listener to open ApplicantDetailActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ApplicantDetailActivity::class.java)
            intent.putExtra("userId", currentUser.userId)
            intent.putExtra("jobId", jobId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = userList.size
}
