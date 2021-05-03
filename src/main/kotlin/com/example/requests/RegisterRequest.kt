package com.example.requests

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val address: String,
)