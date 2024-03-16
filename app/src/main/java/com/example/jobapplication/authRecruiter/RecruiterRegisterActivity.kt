package com.example.jobapplication.authRecruiter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jobapplication.R
import com.example.jobapplication.company.CompanyMainActivity
import com.example.jobapplication.databinding.ActivityRecruiterRegisterBinding
import com.example.jobapplication.models.company
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RecruiterRegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRecruiterRegisterBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var username : EditText
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var createAccount : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecruiterRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        username = binding.username
        email = binding.email
        password = binding.password
        createAccount = binding.button3

        createAccount.setOnClickListener{
            if(username.text.isNotEmpty() && email.text.isNotEmpty() && password.text.isNotEmpty()){
                createAccountWithEmail(username.text.toString(), email.text.toString(), password.text.toString())
            }
            else{
                Toast.makeText(this, "Please fill above fields", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun createAccountWithEmail(username : String, email : String, password : String){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                task -> run {
            if (task.isSuccessful){
                val company = company(
                    companyId = auth.currentUser!!.uid,
                    companyName = username,
                    companyEmail = email
                )

                FirebaseDatabase.getInstance().getReference("Companies")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .setValue(company)
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            Toast.makeText(this@RecruiterRegisterActivity, "Account created successfully!", Toast.LENGTH_LONG).show()
                            var i = Intent(this@RecruiterRegisterActivity, CompanyMainActivity::class.java)
                            startActivity(i)
                            finish()
                        }
                        else{
                            Toast.makeText(this@RecruiterRegisterActivity, "Something went wrong", Toast.LENGTH_LONG).show()
                        }
                    }
//                Toast.makeText(this@RecruiterRegisterActivity, "Account created successfully!", Toast.LENGTH_LONG).show()
//                var i = Intent(this@RecruiterRegisterActivity, CompanyMainActivity::class.java)
//                startActivity(i)
//                finish()

            }else{
                Toast.makeText(this@RecruiterRegisterActivity, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }
        }
    }
}

