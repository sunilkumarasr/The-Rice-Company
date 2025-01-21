package com.shambavi.thericecompany.orders

data class OrderDetails(
    val orderId: String,
    val productImage: String,
    val productName: String,
    val productWeight: String,
    val productDescription: String,
    val price: Double,
    val orderStatus: List<OrderStatus>,
    val shippingDetails: ShippingDetails,
    val billingDetails: BillingDetails,
    var rating: Float? = null
)

data class ShippingDetails(
    val customerName: String,
    val address: String,
    val phoneNumbers: List<String>
)

data class OrderStatus(
    val status: String,
    val date: String,
    val isCompleted: Boolean,
    val statusColor: Int // Resource color ID
)

data class BillingDetails(
    val itemPrice: Double,
    val savings: Double,
    val deliveryCharge: Double,
    val freeDeliveryThreshold: Double
) {
    val grandTotal: Double get() = itemPrice + deliveryCharge
    val remainingForFreeDelivery: Double get() = freeDeliveryThreshold - itemPrice
}
