package com.shambavi.thericecompany.filters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shambavi.thericecompany.databinding.ItemFilterOptionBinding

class FilterOptionsAdapter (
    private val options: List<String>,
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
        holder.bind(options[position])
    }

    override fun getItemCount() = options.size

    inner class OptionViewHolder(private val binding: ItemFilterOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(option: String) {
            binding.tvOption.text = option
            binding.root.isSelected = selectedOptions.contains(option)

            binding.root.setOnClickListener {
                val isSelected = !binding.root.isSelected
                binding.root.isSelected = isSelected
                if (isSelected) {
                    selectedOptions.add(option)
                } else {
                    selectedOptions.remove(option)
                }
                onOptionSelected(option, isSelected)
            }
        }
    }
}
