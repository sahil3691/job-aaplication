package com.example.jobapplication.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobapplication.R
import com.example.jobapplication.adapters.ApplicantsAdapter
import com.example.jobapplication.models.Job
import com.example.jobapplication.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ApplicantsListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var applicantsAdapter: ApplicantsAdapter
    private var userList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_applicants_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val jobId = intent.getStringExtra("jobId")

        recyclerView = findViewById(R.id.applicantsList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        applicantsAdapter = ApplicantsAdapter(this, userList, jobId!!)
        recyclerView.adapter = applicantsAdapter

        loadApplicants()
    }

    private fun loadApplicants() {
        val jobId = intent.getStringExtra("jobId")
        val jobRef = FirebaseDatabase.getInstance().getReference("Jobs").child(jobId!!)
        jobRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val job = snapshot.getValue(Job::class.java)
                if (job != null) {
                    val applicantIds = job.applicants
                    for (applicantId in applicantIds) {
                        val userRef =
                            FirebaseDatabase.getInstance().getReference("Users").child(applicantId)
                        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(userSnapshot: DataSnapshot) {
                                val user = userSnapshot.getValue(User::class.java)
                                user?.let { userList.add(it) }
                                applicantsAdapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle error
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
