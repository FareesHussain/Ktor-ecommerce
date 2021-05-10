package com.example.database.query

import com.example.models.Products
import com.example.models.ShoppingCartResponse
import com.example.requests.ProductAndQuantityList
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.minus
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction


object ShoppingCart: Table("shopping_cart") {
    val cart_id = integer("cart_id")
    val total_amount = integer("total_amount")
    val customer_id = integer("customer_id")
}

object CartProducts: Table("cart_products") {
    val cart_products_id = integer("idcart_products")
    val product_id = integer("product_id")
    val cart_id = integer("cart_id")
    val no_of_products = integer("no_of_products")
    val price_of_products = integer("price_of_products")
}

fun createShoppingCartForCustomer(customers_id: Int) {
    transaction {
        ShoppingCart.insert {
            it[cart_id] = customers_id
            it[total_amount] = 0
            it[customer_id] = customers_id
        }
    }
}

fun removeShoppingCart(cartId: Int) {
    transaction {
        ShoppingCart.deleteWhere {
            ShoppingCart.cart_id eq cartId
        }
    }
}

fun addProductToCart(productId: Int, customerId: Int, price: Int): Boolean {
    var done = false
    transaction {
        runBlocking {
            newSuspendedTransaction {
                CartProducts.insert {
                    it[cart_id] = customerId
                    it[product_id] = productId
                    it[price_of_products] = price
                    it[no_of_products] = 1
                }
                val formerShoppingCart = ShoppingCart.selectAll().having{
                    ShoppingCart.cart_id eq customerId
                }.last()
                ShoppingCart.update( { ShoppingCart.cart_id eq customerId } ) {
                    it[total_amount] = formerShoppingCart[total_amount] - price
                }
                done = true
            }
        }
    }
    return done
}

fun removeProductFromCart(productId: Int, customerID: Int, price: Int): Boolean {
    var done = false
    transaction {
        runBlocking {
            newSuspendedTransaction {
                CartProducts.deleteWhere(limit = 1){
                    CartProducts.cart_id eq customerID and(CartProducts.product_id eq productId)
                }
                val formerShoppingCart = ShoppingCart.selectAll().having{
                    ShoppingCart.cart_id eq customerID
                }.last()
                ShoppingCart.update( { ShoppingCart.cart_id eq customerID } ) {
                    it[total_amount] = formerShoppingCart[total_amount] - price
                }
                done = true
            }
        }
    }
    return done
}

fun updateProductsToCart(productAndQuantityList: ProductAndQuantityList): Int {
    val customersId = productAndQuantityList.customer_id
    val productIDList = productAndQuantityList.product_list
    val quantityList = productAndQuantityList.quantity_list
    var total_price = 0
    transaction {
        runBlocking {
            newSuspendedTransaction {
                CartProducts.deleteWhere {
                    CartProducts.cart_id eq customersId
                }
                for ( i in productIDList.indices) {
                    var product = Product.selectAll().having { Product.product_id eq productIDList[i] }
                    var price = product.last()[Product.price] * quantityList[i]
                    if(checkIfProductExists(productIDList[i])) {
                        CartProducts.insert {
                            it[product_id] = productIDList[i]
                            it[no_of_products] = quantityList[i]
                            it[cart_id] = customersId
                            it[price_of_products] = price
                        }
                    }
                    total_price += price
                }
                ShoppingCart.update( { ShoppingCart.cart_id eq customersId } ) {
                    it[total_amount] = total_price
                }
            }
        }
    }
    return total_price
}

fun getShoppingCart(customers_id: Int): ShoppingCartResponse? {
    var response :ShoppingCartResponse ?= null
    transaction {
        runBlocking {
            newSuspendedTransaction (Dispatchers.Default) {
                val res = ShoppingCart.selectAll().having {
                    ShoppingCart.customer_id eq customers_id
                }
                val shoppingCart = res.last()
                val cartProductsList = ArrayList<Products>()
                val itemQuantityList = ArrayList<Int>()
                val res_products_list = CartProducts.selectAll().having {
                    CartProducts.cart_id eq customers_id
                }
                res_products_list.forEach {
                    cartProductsList.add(
                        getProductForID(id = it[CartProducts.product_id])!!
                    )
                    itemQuantityList.add(
                        it[CartProducts.no_of_products]
                    )
                }
                response = ShoppingCartResponse(
                    customer_id = customers_id,
                    cart_id = customers_id,
                    cartProductsList = cartProductsList,
                    totalAmount = shoppingCart[ShoppingCart.total_amount],
                    quantityCartProducts = itemQuantityList
                )
            }
        }
    }
    return response
}

fun getAllCartProductsForCustomer(cartId: Int): String {
    var json = ""
    transaction {
        val res = CartProducts.selectAll().having {
            CartProducts.cart_id eq cartId
        }
        var c = ArrayList<Products>()
        res.forEach { row ->
            c.add(
                Products(
                    product_id = row[Product.product_id],
                    mobile_name = row[Product.mobile_name],
                    description = row[Product.description],
                    price = row[Product.price],
                    image_url = row[Product.image_url],
                    thumbnail_url = row[Product.thumbnail_url],
                    brand = row[Product.brand],
                )
            )
        }
        json = Gson().toJson(c)
    }
    return json
}
