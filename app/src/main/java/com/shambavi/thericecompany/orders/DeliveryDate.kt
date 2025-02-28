package com.shambavi.thericecompany.orders

data class DeliveryDate(
    val id: Int,
    val date: String,
    var isSelected: Boolean = false
)

data class TimeSlot(
    val id: Int,
    val timeRange: String,
    var isSelected: Boolean = false
)
