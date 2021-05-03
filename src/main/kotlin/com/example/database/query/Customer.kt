package com.example.database.query

import com.example.models.Customers
import com.example.requests.LoginRequest
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransaction

object Customer : Table("customer") {
    val customer_id = integer("id")
    val name = varchar("name", length = 50)
    val email = varchar("email",length = 50)
    val password = varchar("password", length = 50)
    val address = varchar("address", length = 250)
}

fun getAllCustomerData(): String {
    var json = ""
    runBlocking {
        transaction {
            val res = Customer.selectAll().orderBy(Customer.customer_id, false)
            val c = ArrayList<Customers>()
            for (f in res) {
                c.add(
                    Customers(
                        customer_id = f[Customer.customer_id],
                        name = f[Customer.name],
                        email = f[Customer.email],
                        password = f[Customer.password],
                        address = f[Customer.address]
                    )
                )
            }
            json = Gson().toJson(c)
        }
    }
    return json
}

suspend fun loginUser(loginRequest: LoginRequest): Int{
    var customerId = 0
    transaction {
        runBlocking {
            newSuspendedTransaction(Dispatchers.Default) {
                var res = Customer.selectAll().having {
                    Customer.email eq loginRequest.email
                }
                res.forEach {
                    if(it[Customer.password] == loginRequest.password) {
                        customerId = it[Customer.customer_id]
                        return@newSuspendedTransaction
                    }
                }
            }
        }
    }
    return customerId
}

/*
    Returns: the ID of the customer
 */
suspend fun checkIfCustomerExists(customerEmail: String): Int {
    var exists = 0
        transaction {
            runBlocking {
                newSuspendedTransaction(Dispatchers.Default) {
                    var res = Customer.selectAll().having {
                        Customer.email eq customerEmail
                    }.sortedBy {
                        it[Customer.customer_id]
                    }
                    for (row in res) {
                        exists = row[Customer.customer_id]
                        if(exists > 0) return@newSuspendedTransaction
                    }
                }
            }
        }
    return exists
}

suspend fun registerCustomer(customer: Customers){
    transaction {
        runBlocking {
            suspendedTransaction {
                Customer.insert {
                    it[name] = customer.name
                    it[email] = customer.email
                    it[password] = customer.password
                    it[address] = customer.address
                }
            }
        }
    }
}

fun deleteCustomer(customer_id: Int): String {
    var response = "User with id $customer_id doesn't exist"
    transaction {
        var res = Customer.selectAll().orderBy(Customer.customer_id, false)
        for (f in res){
            if(f[Customer.customer_id] == customer_id){
                val customer_name = f[Customer.name]
                Customer.deleteWhere {
                    Customer.customer_id eq customer_id
                }
                response = "User $customer_name deleted Successfully"
                return@transaction
            }
        }
    }
    return response
}

suspend fun getCustomerDetailsForID(customerId: Int): Customers {
    var customer = Customers(0, "0","0","0","0",)
    transaction {
        runBlocking {
            newSuspendedTransaction {
                val res = Customer.selectAll().having {
                    Customer.customer_id eq customerId
                }.last()
                customer = Customers(
                    customer_id = res[Customer.customer_id],
                    name = res[Customer.name],
                    email = res[Customer.email],
                    password = res[Customer.password],
                    address = res[Customer.address],
                )
            }
        }
    }
    return customer
}

suspend fun updateCustomer(customer: Customers): Int {
    var complete = 0
    transaction {
        runBlocking {
            newSuspendedTransaction {
                complete = Customer.update({ Customer.customer_id eq customer.customer_id!! }) {
                    it[name] = customer.name
                    it[email] = customer.email
                    it[password] = customer.password
                    it[address] = customer.address
                }
            }
        }
    }
    return complete
}