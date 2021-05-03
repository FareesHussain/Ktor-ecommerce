package com.example.database.query

import com.example.models.OrderedProducts
import com.example.models.Orders
import com.example.requests.ProductAndQuantityList
import com.google.gson.Gson
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object Order: Table("mobile_order") {
    val order_id = integer("order_id")
    val customer_id = integer("customer_id")
    val total_amount = integer("total_amount")
    val created_on = datetime("created_on")
    val delivery_status = varchar("delivery_status", length = 45)
}

object OrderedProduct: Table("ordered_products"){
    val products_list_id = integer("idordered_products")
    val order_id = integer("order_id")
    val product_id = integer("product_id")
    val no_of_products = integer("no_of_products")
}


fun getAllOrders(customerId: Int?): List<Orders> {
    var orderList = ArrayList<Orders>()
    transaction {
        val res = Order.selectAll().orderBy(Order.order_id)

        for (row in res) {
            orderList.add(
                Orders(
                    order_id = row[Order.order_id],
                    customer_id = row[Order.customer_id],
                    total_amount = row[Order.total_amount],
                    created_on = row[Order.created_on],
                    delivery_status = row[Order.delivery_status]
                )
            )
        }
        customerId?.let {
            orderList.removeIf { it.customer_id != customerId }
        }
    }
    return orderList
}

fun getAllOrdersForCustomer(customerId: Int): String {
    var json = ""
    transaction {
        val res = Order.selectAll().having {
            Order.customer_id eq customerId
        }
        var c = ArrayList<Orders>()
        for (row in res) {
            c.add(
                Orders(
                    order_id = row[Order.order_id],
                    customer_id = row[Order.customer_id],
                    total_amount = row[Order.total_amount],
                    created_on = row[Order.created_on],
                    delivery_status = row[Order.delivery_status]
                )
            )
        }
        json = Gson().toJson(c)
    }
    return json
}

fun addProductsToOrder(productAndQuantityList: ProductAndQuantityList) {
    val customerId = productAndQuantityList.customer_id
    val product_list = productAndQuantityList.product_list
    val quantity_list = productAndQuantityList.quantity_list
    transaction {
        var totalAmount = 0
        var dateTimeNow = DateTime.now()
        val res = Product.selectAll().orderBy(Product.product_id)
        for (row in res) {
            if(row[Product.product_id] in product_list) {
                totalAmount += row[Product.price] * quantity_list[product_list.indexOf(row[Product.product_id])]
            }
        }
        Order.insert {
            it[customer_id] = customerId
            it[total_amount] = totalAmount
            it[created_on] = dateTimeNow
            it[delivery_status] = "Order Placed" // TODO: Make a enum
        }
        val insertedOrder = Order.selectAll().last()
        for (i in product_list.indices){
            OrderedProduct.insert {
                it[order_id] = insertedOrder[Order.order_id]
                it[product_id] = product_list[i]
                it[no_of_products] = quantity_list[i]
            }
        }
    }
}

fun getOrderedProductsForId(
    customerId: Int,
    orderId: Int
):String {
    var json = ""
    transaction {
        var res = OrderedProduct.selectAll().having {
            OrderedProduct.order_id eq orderId
        }
        val c = ArrayList<OrderedProducts>()
        res.forEach {
            c.add(
                OrderedProducts(
                    products_list_id = it[OrderedProduct.products_list_id],
                    order_id = it[OrderedProduct.order_id],
                    product_id = it[OrderedProduct.product_id],
                    no_of_products = it[OrderedProduct.no_of_products],
                )
            )
        }
        json = Gson().toJson(c)
    }
    return json
}

