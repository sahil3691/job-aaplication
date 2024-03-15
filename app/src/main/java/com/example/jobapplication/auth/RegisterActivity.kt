package com.example.jobapplication.auth



import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jobapplication.MainActivity
import com.example.jobapplication.R
import com.example.jobapplication.databinding.ActivityRegisterBinding
import com.example.jobapplication.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var username : EditText
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var createAccount : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
            if(username.text.isNotEmpty() || email.text.isNotEmpty() || password.text.isNotEmpty()){
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
                val user = User(
                    userId = auth.currentUser!!.uid,
                    username = username,
                    email = email
                )

                FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .setValue(user)
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            Toast.makeText(this@RegisterActivity, "Account created successfully!", Toast.LENGTH_LONG).show()
                            var i = Intent(this@RegisterActivity, MainActivity::class.java)
                            startActivity(i)
                            finish()
                        }
                        else{
                            Toast.makeText(this@RegisterActivity, "Something went wrong", Toast.LENGTH_LONG).show()
                        }
                    }
            }else{
                Toast.makeText(this@RegisterActivity, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }
        }
    }
}