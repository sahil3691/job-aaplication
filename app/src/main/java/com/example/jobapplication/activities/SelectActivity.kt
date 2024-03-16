package com.example.jobapplication.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jobapplication.R
import com.example.jobapplication.auth.LoginActivity
import com.example.jobapplication.authRecruiter.RecruiterLoginActivity
import com.example.jobapplication.databinding.ActivitySelectBinding

class SelectActivity : AppCompatActivity() {

    private lateinit var binding : com.example.jobapplication.databinding.ActivitySelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.button1.setOnClickListener {
            val i = Intent(this@SelectActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
        binding.button2.setOnClickListener {
            val i = Intent(this@SelectActivity, RecruiterLoginActivity::class.java)
            startActivity(i)
            finish()
        }

    }
}