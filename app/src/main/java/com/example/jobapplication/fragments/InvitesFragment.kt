package com.example.jobapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobapplication.adapters.InvitesAdapter
import com.example.jobapplication.databinding.FragmentInvitesBinding
import com.example.jobapplication.models.Job
import com.example.jobapplication.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class InvitesFragment : Fragment() {

    private lateinit var binding: FragmentInvitesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var invitesAdapter: InvitesAdapter
    private var jobList = mutableListOf<Job>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInvitesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.applcationsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        invitesAdapter = InvitesAdapter(requireContext(), jobList)
        recyclerView.adapter = invitesAdapter

        // Load jobs based on user's applied jobs
        loadJobs()
    }

    private fun loadJobs() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { uid ->
            val userReference = FirebaseDatabase.getInstance().getReference("Users").child(uid)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    user?.let { loadUserJobs(it) }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    private fun loadUserJobs(user: User) {
        val jobReference = FirebaseDatabase.getInstance().getReference("Jobs")
        jobReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                jobList.clear()
                for (postSnapshot in snapshot.children) {
                    val job = postSnapshot.getValue(Job::class.java)
                    job?.let {
                        if (user.jobs.contains(job.jobId)) {
                            jobList.add(it)
                        }
                    }
                }
                invitesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
