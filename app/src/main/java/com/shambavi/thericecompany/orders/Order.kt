package com.shambavi.thericecompany.orders

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("id"                  ) var id                : String?           = null,
    @SerializedName("order_id"            ) var orderId           : String?           = null,
    @SerializedName("payment_id"          ) var paymentId         : String?           = null,
    @SerializedName("customer_address_id" ) var customerAddressId : String?           = null,
    @SerializedName("amount"              ) var amount            : String?           = null,
    @SerializedName("product_id"          ) var productId         : String?           = null,
    @SerializedName("qty"                 ) var qty               : String?           = null,
    @SerializedName("slotid"              ) var slotid            : String?           = null,
    @SerializedName("cartid"              ) var cartid            : String?           = null,
    @SerializedName("created_at"          ) var createdAt         : String?           = null,
    @SerializedName("status"              ) var status            : String?           = null,
    @SerializedName("full_name"           ) var fullName          : String?           = null,
    @SerializedName("mobile"              ) var mobile            : String?           = null,
    @SerializedName("house_no"            ) var houseNo           : String?           = null,
    @SerializedName("floor"               ) var floor             : String?           = null,
    @SerializedName("landmark"            ) var landmark          : String?           = null,
    @SerializedName("city_town"           ) var cityTown          : String?           = null,
    @SerializedName("state"               ) var state             : String?           = null,
    @SerializedName("country"             ) var country           : String?           = null,
    @SerializedName("zip_code"            ) var zipCode           : String?           = null,
    @SerializedName("slot_date"           ) var slotDate          : String?           = null,
    @SerializedName("start_time"          ) var startTime         : String?           = null,
    @SerializedName("end_time"            ) var endTime           : String?           = null,
    @SerializedName("longitude"            ) var longitude           : String?           = null,
    @SerializedName("latitude"            ) var latitude           : String?           = null,
    @SerializedName("gst_charges"            ) var gst_charges           : String?           = null,
    @SerializedName("products"            ) var products          : ArrayList<OrderProduct> = arrayListOf()
)

data class OrderProduct (



        @SerializedName("id"            ) var id           : String? = null,
        @SerializedName("product_id"    ) var productId    : String? = null,
        @SerializedName("product_title" ) var productTitle : String? = null,
        @SerializedName("product_image" ) var productImage : String? = null,
        @SerializedName("mrp_price"     ) var mrpPrice     : String? = null,
        @SerializedName("market_price"  ) var marketPrice  : String? = null,
        @SerializedName("our_price"     ) var ourPrice     : String? = null,
        @SerializedName("user_rating"   ) var userRating   : String? = null


)
// Enum for Order Status
enum class OrderStatusEnum {
    DELIVERY_EXPECTED,
    CANCELLED,
    DELIVERED,
    REFUNDED
}

