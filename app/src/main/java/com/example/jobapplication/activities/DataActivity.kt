package com.example.jobapplication.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isEmpty
import com.example.jobapplication.MainActivity
import com.example.jobapplication.R
import com.example.jobapplication.databinding.ActivityDataBinding
import com.example.jobapplication.databinding.ActivityMainBinding
import com.example.jobapplication.models.User
import com.example.jobapplication.utils.LoadingDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar

class DataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataBinding
    private lateinit var dateOfBirth:String
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var username : EditText
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var dob : TextView
    private var imageUri : Uri? = null
    private var resume : Uri? = null

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it
        binding.profile.setImageURI(imageUri)
    }

    private val selectResume = registerForActivityResult(ActivityResultContracts.GetContent()){
        resume = it
        binding.resumeStatus.text = resume.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDataBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firebaseAuth = FirebaseAuth.getInstance()

        username = binding.fullName
        dob = binding.dob
        binding.calendar.setOnClickListener{
            clickDatePicker()
        }

        binding.profile.setOnClickListener{
            selectImage.launch("image/*")
        }

        binding.linearLayout.setOnClickListener{
            selectResume.launch("application/pdf")
        }

        binding.submitData.setOnClickListener {
            if (binding.fullName.text.toString().isEmpty() || binding.dob.text.toString().isEmpty() || binding.spinnerGender.isEmpty() || binding.spinnerEducation.isEmpty()){
                Toast.makeText(this, "Please fill above fields", Toast.LENGTH_LONG).show()
            }
            else{
                    uploadData()
            }
        }

    }



    private fun uploadData() {
        if (imageUri != null && resume != null) {
            LoadingDialog.showDialog(this)
            val profileImageRef = FirebaseStorage.getInstance().getReference("profile")
                .child(firebaseAuth.currentUser!!.uid).child("profile.jpg")
            val resumeRef = FirebaseStorage.getInstance().getReference("resume")
                .child(firebaseAuth.currentUser!!.uid).child("resume.pdf")

            profileImageRef.putFile(imageUri!!)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    profileImageRef.downloadUrl
                }
                .addOnCompleteListener { profileImageTask ->
                    if (profileImageTask.isSuccessful) {
                        val profileImageUrl = profileImageTask.result.toString()
                        resumeRef.putFile(resume!!)
                            .continueWithTask { task ->
                                if (!task.isSuccessful) {
                                    task.exception?.let {
                                        throw it
                                    }
                                }
                                resumeRef.downloadUrl
                            }
                            .addOnCompleteListener { resumeTask ->
                                if (resumeTask.isSuccessful) {
                                    val resumeUrl = resumeTask.result.toString()
                                    storeData(profileImageUrl, resumeUrl)
                                } else {
                                    LoadingDialog.hideDialog()
                                    Toast.makeText(this, resumeTask.exception?.message, Toast.LENGTH_LONG).show()
                                }
                            }
                    } else {
                        LoadingDialog.hideDialog()
                        Toast.makeText(this, profileImageTask.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        } else {
            Toast.makeText(this, "Please select a profile image and resume", Toast.LENGTH_LONG).show()
        }
    }

    private fun storeData(profileImageUrl: String, resumeUrl: String) {
        val data = User(
            userId = firebaseAuth.currentUser!!.uid,
            username = username.text.toString(),
            image = profileImageUrl,
            resume = resumeUrl,
            education = binding.education.toString(), // Ensure education field is correctly bound
            gender = binding.gender.toString(), // Ensure gender field is correctly bound
            dob = dob.text.toString()
        )

        FirebaseDatabase.getInstance().getReference("Users")
            .child(firebaseAuth.currentUser!!.uid)
            .setValue(data)
            .addOnCompleteListener { task ->
                LoadingDialog.hideDialog()
                if (task.isSuccessful) {
                    Toast.makeText(this, "User Register Successful", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@DataActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }


    private fun clickDatePicker(){
        val myCalendar= Calendar.getInstance()
        val year= myCalendar.get(Calendar.YEAR)
        val month= myCalendar.get(Calendar.MONTH)
        val day=myCalendar.get(Calendar.DAY_OF_MONTH)


        val dpd= DatePickerDialog(this, DatePickerDialog.OnDateSetListener{
                _, year, month,dayOfMonth ->
            // Toast.makeText(this, "Year was $year, month is ${month+1} and the date is $dayOfMonth", Toast.LENGTH_LONG).show()
            val data="$dayOfMonth/${month+1}/$year"
            dateOfBirth = data.toString()
            binding.dob.text = data

        },
            year,month,day
        )
        dpd.datePicker.maxDate = System.currentTimeMillis()-86400000
        dpd.show()


    }




}