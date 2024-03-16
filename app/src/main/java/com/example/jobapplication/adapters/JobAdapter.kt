package com.example.jobapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobapplication.R
import com.example.jobapplication.activities.JobDetailActivity
import com.example.jobapplication.models.Job


class JobAdapter(private val context: Context, private var jobList: List<Job>) :
    RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val positionTextView: TextView = itemView.findViewById(R.id.job_position_tv)
        val descriptionTextView: TextView = itemView.findViewById(R.id.job_description_tv)
        val companyNameTextView: TextView = itemView.findViewById(R.id.company_name_tv)
        val dateTextView: TextView = itemView.findViewById(R.id.date_tv)
        val durationTextView: TextView = itemView.findViewById(R.id.duration_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_job, parent, false)
        return JobViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val currentItem = jobList[position]
        holder.positionTextView.text = currentItem.postion
        holder.descriptionTextView.text = currentItem.description
        holder.companyNameTextView.text = currentItem.companyName
        holder.dateTextView.text = currentItem.salary
        holder.durationTextView.text = currentItem.duration

        holder.itemView.setOnClickListener {
            // When item is clicked, navigate to JobDetailActivity with job ID
            val intent = Intent(context, JobDetailActivity::class.java).apply {
                putExtra("jobId", currentItem.jobId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = jobList.size

    fun updateList(newList: List<Job>) {
        jobList = newList
        notifyDataSetChanged()
    }
}
