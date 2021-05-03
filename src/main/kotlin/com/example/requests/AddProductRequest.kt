package com.example.requests

data class AddProductRequest(
    val mobile_name: String,
    val description: String,
    val price: Int,
    val image_url: String,
    val thumbnail_url: String,
    val brand: String
)