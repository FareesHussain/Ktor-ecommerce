package com.example.database.query

import com.example.models.Products
import com.example.requests.AddProductRequest
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction


object Product: Table("product") {
    val product_id = integer("product_id")
    val mobile_name = varchar("mobile_name", length = 100)
    val description = varchar("description", length = 1000)
    val price = integer("price")
    val image_url = varchar("image_url", length = 200)
    val thumbnail_url = varchar("thumbnail_url", length = 200)
    val brand =  varchar("brand", length = 45)
}

suspend fun getAllProducts(brand: String?): List<Products> {
    val listOfProducts = ArrayList<Products>()
    transaction {
        runBlocking {
            newSuspendedTransaction {
                val res = Product.selectAll().orderBy(Product.product_id, false)
                for (f in res) {
                    listOfProducts.add(
                        Products(
                            product_id = f[Product.product_id],
                            mobile_name = f[Product.mobile_name],
                            description = f[Product.description],
                            price = f[Product.price],
                            image_url = f[Product.image_url],
                            thumbnail_url = f[Product.thumbnail_url],
                            brand = f[Product.brand],
                        )
                    )
                }
                brand?.let { brand ->
                    if(brand.isNotEmpty()) listOfProducts.filter { it.brand == brand }
                }
            }
        }
    }
    return listOfProducts
}

suspend fun addProduct(product: AddProductRequest): String {
    var response = ""
    transaction {
        runBlocking {
            newSuspendedTransaction(Dispatchers.Default) {
                Product.insert {
                    it[mobile_name] = product.mobile_name
                    it[description] = product.description
                    it[price] = product.price
                    it[image_url] = product.image_url
                    it[thumbnail_url] = product.thumbnail_url
                    it[brand] = product.brand
                }
                val res = Product.selectAll().last()
                response = "Product ${res[Product.product_id]} Added successfully"
            }
        }
    }
    return response
}
suspend fun getProductForID(id: Int): Products? {
    var res: Products?=null
    transaction {
        runBlocking {
            newSuspendedTransaction {
                val product = Product.selectAll().having {
                    Product.product_id eq id
                }
                product.last().apply {
                    res = Products(
                        this[Product.product_id],
                        this[Product.mobile_name],
                        this[Product.description],
                        this[Product.price],
                        this[Product.image_url],
                        this[Product.thumbnail_url],
                        this[Product.brand],
                    )
                }
            }
        }
    }
    return res
}

fun checkIfProductExists(productId: Int): Boolean{
    var exists = false
    transaction {
        runBlocking {
            newSuspendedTransaction {
                val res = Product.selectAll().having { Product.product_id eq productId }
                val product = res.last()
                exists = product[Product.product_id] == productId
            }
        }
    }
    return exists
}
fun removeProduct(product_id: Int){
    transaction {
        Product.deleteWhere {
            Product.product_id eq product_id
        }
    }
}

fun updateProduct(product: Products) {
    transaction {
        Product.update({ Product.product_id eq product.product_id }) {
            it[mobile_name] = product.mobile_name
            it[description] = product.description
            it[price] = product.price
            it[image_url] = product.image_url
            it[thumbnail_url] = product.thumbnail_url
            it[brand] = product.brand
        }
    }
}
