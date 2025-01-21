package com.shambavi.thericecompany.orders

data class Order(
    val id: String,
    val productImage: String,
    val productName: String,
    val orderStatus: OrderStatusEnum,
    val deliveryDate: String,
    val rating: Float? = null
)

// Enum for Order Status
enum class OrderStatusEnum {
    DELIVERY_EXPECTED,
    CANCELLED,
    DELIVERED,
    REFUNDED
}

