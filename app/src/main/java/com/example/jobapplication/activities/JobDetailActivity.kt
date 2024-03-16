package com.example.jobapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jobapplication.MainActivity
import com.example.jobapplication.R
import com.example.jobapplication.databinding.ActivityJobDetailBinding
import com.example.jobapplication.models.Job
import com.example.jobapplication.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class JobDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobDetailBinding
    private lateinit var position : TextView
    private lateinit var description : TextView
    private lateinit var skills : TextView
    private lateinit var workExperience : TextView
    private lateinit var salary : TextView
    private lateinit var duration : TextView
    private lateinit var location : TextView
    private lateinit var companyName : TextView
    private lateinit var companyEmail : TextView
    private lateinit var applyBtn : Button
    private lateinit var jobId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        jobId = intent.getStringExtra("jobId").toString()

        position = binding.jobPosition
        description = binding.jobDescriptionTv1
        skills = binding.jobSkills
        workExperience = binding.tvExperience
        salary = binding.jobSalary
        duration = binding.tvDuration
        location = binding.tvLocation
        companyName = binding.tvCompanyName
        companyEmail = binding.tvCompanyEmail
        applyBtn = binding.applyBtn

        val jobRef = FirebaseDatabase.getInstance().getReference("Jobs").child(jobId)
        jobRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val job = snapshot.getValue(Job::class.java)
                if (job!=null){
                    position.text = job.postion
                    description.text = job.description
                    skills.text = job.skills
                    workExperience.text = job.workExperience
                    salary.text = job.salary
                    duration.text = job.duration
                    location.text = job.location
                    companyName.text = job.companyName

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        applyBtn.setOnClickListener {
            val jobRef1 = FirebaseDatabase.getInstance().getReference("Jobs").child(jobId)
            jobRef1.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val job = snapshot.getValue(Job::class.java)
                    if (job != null) {
                        val applicantId = FirebaseAuth.getInstance().currentUser!!.uid
                        job.applicants.add(applicantId)
                        val updates = HashMap<String, Any>()
                        updates["applicants"] = job.applicants
                        jobRef1.updateChildren(updates)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                                    val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
                                    userRef.addListenerForSingleValueEvent(object : ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                           val user = snapshot.getValue(User::class.java)
                                            if(user!=null) {
                                                user.jobs.add(jobId)
                                                userRef.setValue(user).addOnCompleteListener {
                                                    Toast.makeText(
                                                        this@JobDetailActivity,
                                                        "Applied for the Job Successfully",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                    startActivity(
                                                        Intent(
                                                            this@JobDetailActivity,
                                                            MainActivity::class.java
                                                        )
                                                    )
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }

                                    })
                                } else {
                                    Toast.makeText(
                                        this@JobDetailActivity,
                                        "Failed to apply for the job",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }




    }
}