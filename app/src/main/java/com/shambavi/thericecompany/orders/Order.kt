package com.shambavi.thericecompany.orders

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("id"                  ) var id                : String? = null,
    @SerializedName("order_id"            ) var orderId           : String? = null,
    @SerializedName("payment_id"          ) var paymentId         : String? = null,
    @SerializedName("customer_address_id" ) var customerAddressId : String? = null,
    @SerializedName("amount"              ) var amount            : String? = null,
    @SerializedName("product_id"          ) var productId         : String? = null,
    @SerializedName("qty"                 ) var qty               : String? = null,
    @SerializedName("slotid"              ) var slotid            : String? = null,
    @SerializedName("cartid"              ) var cartid            : String? = null,
    @SerializedName("created_at"          ) var createdAt         : String? = null,
    @SerializedName("product_title"       ) var productTitle      : String? = null,
    @SerializedName("product_image"       ) var productImage      : String? = null,
    @SerializedName("status"              ) var orderStatus            : String = ""
)

// Enum for Order Status
enum class OrderStatusEnum {
    DELIVERY_EXPECTED,
    CANCELLED,
    DELIVERED,
    REFUNDED
}

