package com.example.responses

import com.google.gson.Gson

data class KMobileResponse(
    val successful: Boolean,
    val message: String,
)
data class KMobileAnyResponse(
    val successful: Boolean,
    val message: Any,
)

data class KMobileResponseWithData(
    val successful: Boolean,
    val message: String,
    val data: Any?,
)

fun getResponse(successful: Boolean, message: String):String {
    return Gson().toJson(KMobileResponse(successful, message))
}
fun getAnyRes(successful: Boolean, dataClass: Any):String {
    return Gson().toJson(KMobileAnyResponse(successful, dataClass))
}
