package com.example.loginapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginapp.R
import com.example.loginapp.dao.UserDao
import com.example.loginapp.databinding.ActivityCreateAccountBinding
import com.example.loginapp.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateAccount : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Utils.hideAppBar(this@CreateAccount)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference();



        binding.signUpCreate.setOnClickListener {
            val email = binding.emailCreate.text.toString()
            val password = binding.passwordCreate.text.toString()
            val confirmPassword = binding.confirmPasswordCreate.text.toString()

            binding.progressCircular.visibility = View.VISIBLE


            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Validate email format
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.progressCircular.visibility = View.GONE
                    if (task.isSuccessful) {
                        // Account creation success
                        Toast.makeText(
                            this,
                            "Congratulation, Account has been created ",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Proceed to next activity or perform other tasks


                        val user = auth.currentUser
                        if (user != null) {

                            saveUserDetails(user.uid, email, password)
                            Log.e("Creation-->", "user.uid --> {$user.uid} " )
                            Log.e("Creation-->", "email --> $email " )
                            Log.e("Creation-->", "password --> $password " )

                        }




                        startActivity(Intent(this@CreateAccount, LoginActivity::class.java))
                        finish()


                    } else {

                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    private fun saveUserDetails(userId: String, email: String, password: String) {
        val user = UserDao(userId, email, password)
        database.child("users").child(userId).setValue(user)
    }

}