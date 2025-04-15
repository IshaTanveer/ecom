package com.example.ecommerce

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.ecommerce.databinding.ActivityOpeningScreenBinding

class openingScreen : AppCompatActivity() {

    private lateinit var binding: ActivityOpeningScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityOpeningScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(homeFrag())

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeIcon -> replaceFragment(homeFrag())
                R.id.messageIcon -> replaceFragment(chatFrag())
                R.id.cartIcon -> replaceFragment(cartFrag())
                R.id.accountIcon -> replaceFragment(accountFrag())
                else -> {
                }
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragFrame, fragment)
        fragmentTransaction.commit()
    }

}