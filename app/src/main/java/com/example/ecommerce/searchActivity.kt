package com.example.ecommerce

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce.databinding.ActivitySearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class searchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var productList: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        goBackToHome()
        firebaseRef = FirebaseDatabase.getInstance().getReference("products")

        makeSearch()
        val layoutManager = GridLayoutManager(this, 2)
        binding.searchedProducts.layoutManager = layoutManager

    }

    private fun makeSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@searchActivity, "Search submitted: $query", Toast.LENGTH_SHORT).show()
                searching(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(this@searchActivity, "Filtering in real time", Toast.LENGTH_SHORT).show()
                return true
            }

        })
    }

    private fun searching(query: String?) {
        productList = arrayListOf()
        val searchQuery = firebaseRef.orderByChild("name")
            .startAt(query!!.lowercase())
            .endAt(query!!.lowercase() + "\uf8ff")

        searchQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                if (snapshot.exists()){
                    for (productSnap in snapshot.children){
                        val product = productSnap.getValue(Product::class.java)
                        if(product!= null)
                            productList.add(product)
                        else
                            Log.e("Firebase", "Product is null at: ${productSnap.key}")
                    }
                }
                val myAdapter = ProductAdapter(productList)
                binding.searchedProducts.adapter = myAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@searchActivity, "error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun goBackToHome() {
        binding.backToHome.setOnClickListener{
            finish()
        }
    }
}