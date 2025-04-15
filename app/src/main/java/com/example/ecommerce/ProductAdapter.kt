package com.example.ecommerce

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.databinding.ProductOverviewBinding

class ProductAdapter(private var product:ArrayList<Product>):
    RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ProductOverviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ProductOverviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return product.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentProduct = product[position]

        holder.binding.productName.text = currentProduct.name
        holder.binding.productprice.text = currentProduct.price.toString()
        holder.binding.productDiscount.text = currentProduct.discount.toString()
        holder.binding.productLocation.text = currentProduct.location
        Glide.with(holder.itemView.context)
            .load(currentProduct.photo)
            .error(R.drawable.warning)
            .into(holder.binding.productPhoto)
    }

}