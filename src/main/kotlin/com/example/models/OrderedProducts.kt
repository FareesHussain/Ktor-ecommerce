package com.example.models

data class OrderedProducts(
    val products_list_id: Int,
    val order_id: Int,
    val product_id: Int,
    val no_of_products: Int,
)
