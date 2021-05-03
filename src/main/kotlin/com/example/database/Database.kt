package com.example.database

import com.example.models.Customers
import com.example.models.OrderedProducts
import com.example.models.Orders
import com.example.models.Products
import com.google.gson.Gson
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

private const val sql_user = "KMobile_user"
private const val sql_pass = "kmobilepass"
private const val sql_db = "KMobile_db"

fun connectToDatabase() {
    val url = getURL(
        username = sql_user,
        password = sql_pass,
        database = sql_db
    )
    val driver = "com.mysql.cj.jdbc.Driver"
    Database.connect(url, driver)
}

fun getURL(username:String, password: String, database: String) =
    "jdbc:mysql://$username:$password@localhost:3306/$database?useUnicode=true&serverTimezone=UTC"


