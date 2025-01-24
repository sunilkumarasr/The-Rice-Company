package com.shambavi.thericecompany.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shambavi.thericecompany.databinding.ItemAddressBinding

class AddressAdapter : ListAdapter<AddressModel, AddressAdapter.AddressViewHolder>(AddressDiffCallback()) {

    private var selectedPosition = -1
    private var bindingAdapterPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = ItemAddressBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        bindingAdapterPosition=holder.adapterPosition
        holder.bind(getItem(position))
    }

    inner class AddressViewHolder(private val binding: ItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(address: AddressModel) {
            binding.apply {
                tvName.text = address.name
                tvAddress.text = address.address
                tvPhone.text = address.phone.takeIf { it.isNotEmpty() }
                rbAddress.isChecked = address.isSelected

                root.setOnClickListener {
                    if (selectedPosition != bindingAdapterPosition) {
                        val previousSelected = selectedPosition
                        selectedPosition = bindingAdapterPosition
                        notifyItemChanged(previousSelected)
                        notifyItemChanged(selectedPosition)
                    }
                }

                ivEdit.setOnClickListener {
                    // Handle edit
                }

                ivDelete.setOnClickListener {
                    // Handle delete
                }
            }
        }
    }
}
