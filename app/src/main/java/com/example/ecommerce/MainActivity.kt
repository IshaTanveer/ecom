package com.example.ecommerce

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecommerce.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth

fun replaceActivity(context: Context, target: Class<out Activity>) {
    val intent = Intent(context, target)
    context.startActivity(intent)
    if (context is Activity) {
        context.finish()
    }
}
fun startNewActivity(context: Context, target: Class<out Activity>){
    val intent = Intent(context, target)
    context.startActivity(intent)
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        signupFun()
        openLoginPage()

    }

    private fun openLoginPage() {
        binding.loginNowButton.setOnClickListener{
            replaceActivity(this, loginActivity::class.java)
        }
    }

    private fun signupFun() {
        binding.signupButton.setOnClickListener{
            val email = binding.emailTextEdit.text.toString()
            val password = binding.passwordTextEdit.text.toString()

            if (listOf(email, password).any { it.isEmpty() })  //if(name.isEmpty() || email.isEmpty() || password.isEmpty())
                Toast.makeText(this, "One of the required fields is empty", Toast.LENGTH_SHORT).show()
            else{
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                else if(!isStrongPassword(password))
                    Toast.makeText(this, "Password must be at least 8 characters and include a special character", Toast.LENGTH_SHORT).show()
                else{
                    makeNewUser(email, password)
                }
            }

        }
    }

    private fun makeNewUser(email: String, password: String) {
        binding.signupProgressBar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    binding.signupProgressBar.visibility = View.GONE
                    Toast.makeText(baseContext, "Authentication successful.", Toast.LENGTH_SHORT,).show()
                    replaceActivity(this, openingScreen::class.java)
                } else {
                    binding.signupProgressBar.visibility = View.GONE
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT,).show()
                }
            }
    }

    private fun isStrongPassword(password: String): Boolean {
        val passwordPattern = Regex("^(?=.*[!@#\$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).{8,}$")
        return password.matches(passwordPattern)
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            replaceActivity(this, openingScreen::class.java)
        }
    }

}