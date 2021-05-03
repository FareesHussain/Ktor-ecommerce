package com.example.requests

data class ProductAndQuantityList(
    val customer_id: Int,
    val product_list: List<Int>,
    val quantity_list: List<Int>
)
