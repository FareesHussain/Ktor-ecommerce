package com.example.requests

data class CartProductEdit(
    val customer_id: Int,
    val product_id: Int,
    val price: Int
)