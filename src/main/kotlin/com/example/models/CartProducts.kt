package com.example.models

data class CartProducts(
    val cart_product_id: Int,
    val product_id: Int,
    val cart_id: Int,
    val no_of_products: Int,
    val price_of_products: Int
)
