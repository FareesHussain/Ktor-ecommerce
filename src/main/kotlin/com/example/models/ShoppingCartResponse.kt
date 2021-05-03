package com.example.models

data class ShoppingCartResponse(
    val customer_id: Int,
    val cart_id: Int,
    val cartProductsList: ArrayList<Products>,
    val quantityCartProducts: ArrayList<Int>,
    val totalAmount: Int
)
