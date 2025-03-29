package com.shambavi.thericecompany.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.AddressData
import com.shambavi.thericecompany.databinding.ItemAddressBinding
import com.shambavi.thericecompany.listeners.ProductListener

class AddressAdapter : ListAdapter<AddressData, AddressAdapter.AddressViewHolder>(AddressDiffCallback()) {

    private var selectedPosition = -1
    private var bindingAdapterPosition = -1

   lateinit var listner:ProductListener
    fun setListners(listner:ProductListener){
        this.listner=listner
    }
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

        fun bind(address: AddressData) {
            binding.apply {
                tvName.text = address.fullName
                tvAddress.text = formAddress(address)
                tvPhone.text = address.mobile
                rbAddress.isChecked = address.isSelected

                root.setOnClickListener {
                    if (selectedPosition != bindingAdapterPosition) {
                        val previousSelected = selectedPosition
                        selectedPosition = bindingAdapterPosition
                        notifyItemChanged(previousSelected)
                        notifyItemChanged(selectedPosition)
                    }
                    address.id?.let { it1 ->
                        MyPref.setAddress(binding.tvName.context,
                            it1,tvAddress.text.toString())
                    }
                    listner.addProduct("","")
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

    fun formAddress(data:AddressData):String
    {
        var adrs="${data.houseNo},${data.floor},${data.landmark}\n${data.cityTown},${data.state},${data.country},${data.zipCode}"
        adrs=adrs.replace(",,",",")
        return adrs
    }
}
