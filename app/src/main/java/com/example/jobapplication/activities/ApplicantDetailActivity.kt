package com.example.jobapplication.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.buzzhub.notification.NotificationData
import com.example.buzzhub.notification.PushNotification
import com.example.buzzhub.notification.api.ApiUtilities
import com.example.jobapplication.company.CompanyMainActivity
import com.example.jobapplication.databinding.ActivityApplicantDetailBinding
import com.example.jobapplication.models.User
import com.google.firebase.database.*
import com.rajat.pdfviewer.PdfViewerActivity
import com.rajat.pdfviewer.util.saveTo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplicantDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApplicantDetailBinding
    private lateinit var userId: String
    private lateinit var jobId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplicantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("userId") ?: ""
        jobId = intent.getStringExtra("jobId") ?:""

        loadUserData()
        setupPdfView()

        binding.acceptBtn.setOnClickListener {
           val  firebaseRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
            firebaseRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if(user!=null){
                        user.jobs.add(jobId)
                        firebaseRef.setValue(user).addOnCompleteListener {
                            sendNotification(user.username + " your resume has been selected for the job ", user.userId)
                            Toast.makeText(this@ApplicantDetailActivity, "USERNAME = " + user.username, Toast.LENGTH_LONG).show()
                            Toast.makeText(this@ApplicantDetailActivity, "Application Accepted successfully", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@ApplicantDetailActivity, CompanyMainActivity::class.java))
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

        binding.rejectBtn.setOnClickListener {
            Toast.makeText(this@ApplicantDetailActivity, "Application Rejected successfully", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@ApplicantDetailActivity, CompanyMainActivity::class.java))
        }

    }

    private fun loadUserData() {
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    // Set user details in the UI
                    binding.userFullName.text = user.username
                    binding.skillTv.text = user.skills
                    binding.educationTv.text = user.education
                    binding.genderTv.text = user.gender
                    binding.dobTv.text = user.dob
                    // Load user image using Glide
                    Glide.with(this@ApplicantDetailActivity)
                        .load(user.image) // Error image if loading fails
                        .into(binding.dpImage)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun setupPdfView() {
        binding.linearLayout4.setOnClickListener {
            val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    val pdfUrl = user?.resume ?: ""
                    if (pdfUrl.isNotEmpty()) {
                        startActivity(
                            PdfViewerActivity.launchPdfFromUrl(
                                context = this@ApplicantDetailActivity,
                                pdfUrl = pdfUrl,
                                pdfTitle = "PDF Title",
                                saveTo = saveTo.ASK_EVERYTIME,
                                enableDownload = true
                            )
                        )
                    } else {
                        Toast.makeText(this@ApplicantDetailActivity, "PDF URL is empty", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }


    private fun sendNotification(message: String, userId : String) {

        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val receiverUser = snapshot.getValue(User::class.java)
                if (receiverUser!=null) {
                    val notificationData = PushNotification(
                        NotificationData(message, message),
                        receiverUser.fcmToken
                    )

                    ApiUtilities.getInstance().sendNotification(
                        notificationData
                    ).enqueue(object : Callback<PushNotification> {
                        override fun onResponse(
                            call: Call<PushNotification>,
                            response: Response<PushNotification>
                        ) {
                            // Toast.makeText(this@ProfileActivity,"Notification sent!", Toast.LENGTH_LONG).show()
                        }

                        override fun onFailure(call: Call<PushNotification>, t: Throwable) {
                            Toast.makeText(this@ApplicantDetailActivity, "Something went wrong", Toast.LENGTH_LONG).show()
                        }

                    })

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }



}
