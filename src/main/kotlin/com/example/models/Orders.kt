package com.example.models

import org.joda.time.DateTime

data class Orders(
    val order_id: Int,
    val customer_id: Int,
    val total_amount: Int,
    val created_on: DateTime,
    val delivery_status: String
)
