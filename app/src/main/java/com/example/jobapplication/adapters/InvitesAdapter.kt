package com.example.jobapplication.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobapplication.R
import com.example.jobapplication.models.Job

class InvitesAdapter(private val context: Context, private val jobList: List<Job>) :
    RecyclerView.Adapter<InvitesAdapter.JobViewHolder>() {

    class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val positionTextView: TextView = itemView.findViewById(R.id.job_position_tv)
        val descriptionTextView: TextView = itemView.findViewById(R.id.job_description_tv)
        val companyNameTextView: TextView = itemView.findViewById(R.id.company_name_tv1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.application_item, parent, false)
        return JobViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val currentItem = jobList[position]
        holder.positionTextView.text = currentItem.postion
        holder.descriptionTextView.text = currentItem.description
        holder.companyNameTextView.text = currentItem.companyName
    }

    override fun getItemCount() = jobList.size
}
