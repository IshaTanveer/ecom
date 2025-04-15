package com.example.ecommerce

data class Product(
    var discount:Int = 0,
    var location:String = "",
    var name:String = "",
    var photo: String = "",
    var price:Int = 0
)
