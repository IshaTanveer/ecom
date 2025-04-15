package com.example.ecommerce

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecommerce.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class loginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loginUser()
        switchToSignup()

    }

    private fun switchToSignup() {
        binding.switchAccountButton.setOnClickListener{
            replaceActivity(this, MainActivity::class.java)
        }
    }

    private fun loginUser() {
        binding.loginButton.setOnClickListener{
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()
            if (listOf(email, password).any { it.isEmpty() })  //if(name.isEmpty() || email.isEmpty() || password.isEmpty())
                Toast.makeText(this, "One of the required fields is empty", Toast.LENGTH_SHORT).show()
            else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            else{
                signinUser(email, password)
            }
                //Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signinUser(email: String, password: String) {
        binding.loginProgressBar.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    binding.loginProgressBar.visibility = View.GONE
                    Toast.makeText(baseContext, "Login successful", Toast.LENGTH_SHORT,).show()
                    val user = auth.currentUser
                    if (user != null)
                        replaceActivity(this, openingScreen::class.java)
                } else {
                    binding.loginProgressBar.visibility = View.GONE
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

}