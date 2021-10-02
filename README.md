### K-Mobile Database using [Ktor](https://ktor.io/)







#### API documentation

- Register Customer (POST -> /register_customer)
    - Input 
        - Customer attributes except id,
        - 
    - Functionality
        - Add Customer to Customer table
        - Add Cart to Shopping cart table

- Update Customer (POST -> /register_customer)
    - Input 
        - Customer attributes including id
    - Functionality
        - Add Customer to Customer table
        - Add Cart to Shopping cart table

- Remove Customer (POST -> /remove_customer)
    - Input 
        - customer_id --> Int,
    - Functionality
        - Remove Customer from Customer table
        - Remove Cart from Shopping cart table
    




#### Admin APIs

- Show All Customers (GET -> /admin_all_customers)
    - Input
        - N/A
    - Functionality
        - Shows list of all customers


- Remove Products
