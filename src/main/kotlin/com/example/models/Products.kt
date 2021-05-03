package com.example.models

data class Products(
    val product_id: Int,
    val mobile_name: String,
    val description: String,
    val price: Int,
    val image_url: String,
    val thumbnail_url: String,
    val brand: String
)
