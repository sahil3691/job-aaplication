package com.example.jobapplication.authRecruiter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jobapplication.R
import com.example.jobapplication.auth.RegisterActivity
import com.example.jobapplication.company.CompanyMainActivity
import com.example.jobapplication.databinding.ActivityRecruiterLoginBinding
import com.example.jobapplication.models.company
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class RecruiterLoginActivity : AppCompatActivity() {


    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var binding : ActivityRecruiterLoginBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecruiterLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser

        if (currentUser != null) {
            // The user is already signed in, navigate to MainActivity
            val intent = Intent(this, CompanyMainActivity::class.java)
            startActivity(intent)
            finish() // finish the current activity to prevent the user from coming back to the SignInActivity using the back button
        }

        val signUpBtn : Button = binding.signup

        binding.button3.setOnClickListener {
            if(binding.email.text.toString().isNotEmpty() && binding.password.text.toString().isNotEmpty()){
                signInWithEmail(binding.email.text.toString(), binding.password.text.toString())
            }else{
                Toast.makeText(this, "Please fill above fields!", Toast.LENGTH_LONG).show()
            }
        }

        signUpBtn.setOnClickListener {
            val intent = Intent(this@RecruiterLoginActivity, RecruiterRegisterActivity::class.java)
            startActivity(intent)
        }

        binding.google.setOnClickListener {
            signIn()
        }

    }


    private fun signInWithEmail(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
            run {
                if (task.isSuccessful) {
                    val intent = Intent(this, CompanyMainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RecruiterLoginActivity.RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RecruiterLoginActivity.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val company =  company(
                        companyId =  auth.currentUser!!.uid,
                        companyName = auth.currentUser!!.displayName!!,
                        companyEmail = auth.currentUser!!.email!!
                    )
                    FirebaseDatabase.getInstance().getReference("Companies")
                        .child(auth.currentUser!!.uid)
                        .setValue(company)
                        .addOnCompleteListener{
                            if (it.isSuccessful){
                                Toast.makeText(this, "Signed in as ${company.companyName}", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, CompanyMainActivity::class.java))
                                finish()
                            }
                            else{
                                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                            }
                        }
//                    Toast.makeText(this, "Signed in as ${auth.currentUser!!.displayName}", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this, CompanyMainActivity::class.java))
//                    finish()
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

}




