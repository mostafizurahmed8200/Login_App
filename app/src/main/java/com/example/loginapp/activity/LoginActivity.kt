package com.example.loginapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginapp.R
import com.example.loginapp.databinding.ActivityLoginBinding
import com.example.loginapp.utils.Utils
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Utils.hideAppBar(this@LoginActivity)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializationCode()

        auth = FirebaseAuth.getInstance()
        binding.loginBtn.setOnClickListener {

            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            binding.progressCircular.visibility = View.VISIBLE

            if (email.isEmpty() || password.isEmpty()) {
                binding.progressCircular.visibility = View.GONE
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            signInWithEmailAndPassword(email, password)

        }

    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.progressCircular.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()

                } else {
                    Toast.makeText(baseContext, "email or password is wrong !", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun initializationCode() {
        binding.createAccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity, CreateAccount::class.java))
        }
        binding.forgetPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgetPassword::class.java))
        }
    }
}