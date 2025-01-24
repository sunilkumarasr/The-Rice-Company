package com.shambavi.thericecompany.cart

import androidx.recyclerview.widget.DiffUtil

class AddressDiffCallback : DiffUtil.ItemCallback<AddressModel>() {
    override fun areItemsTheSame(oldItem: AddressModel, newItem: AddressModel) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: AddressModel, newItem: AddressModel) =
        oldItem == newItem
}
