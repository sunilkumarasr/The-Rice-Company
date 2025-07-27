package com.shambavi.thericecompany.filters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gadiwalaUser.Models.PriceRange
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ItemFilterOptionBinding

class FilterOptionsAdapter (
    private val options: ArrayList<PriceRange>,
    private val onOptionSelected: (String, Boolean) -> Unit
) : RecyclerView.Adapter<FilterOptionsAdapter.OptionViewHolder>() {

    private val selectedOptions = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val binding = ItemFilterOptionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        options[position]?.let { holder.bind(it)  }
    }

    override fun getItemCount() = options.size

    inner class OptionViewHolder(private val binding: ItemFilterOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(option: PriceRange) {
            binding.tvOption.text = option.price

            binding.root.isSelected = selectedOptions.contains(option.id)
            if(binding.root.isSelected)
            {
                binding.tvOption.setTextColor(binding.root.context.getColor(R.color.white))
            }else
            {
                binding.tvOption.setTextColor(binding.root.context.getColor(R.color.black))
            }
            binding.root.setOnClickListener {
                val isSelected = !binding.root.isSelected
                binding.root.isSelected = isSelected
                if (isSelected) {
                    selectedOptions.add(option.id!!)
                } else {
                    selectedOptions.remove(option.id!!)
                }
                onOptionSelected(option.id!!, isSelected)
                notifyDataSetChanged()
            }
        }
    }
}
