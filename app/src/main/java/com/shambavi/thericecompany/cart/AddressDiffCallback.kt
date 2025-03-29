package com.shambavi.thericecompany.cart

import androidx.recyclerview.widget.DiffUtil
import com.gadiwalaUser.Models.AddressData

class AddressDiffCallback : DiffUtil.ItemCallback<AddressData>() {
    override fun areItemsTheSame(oldItem: AddressData, newItem: AddressData) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: AddressData, newItem: AddressData) =
        oldItem == newItem
}
