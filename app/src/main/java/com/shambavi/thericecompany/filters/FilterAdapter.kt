package com.shambavi.thericecompany.filters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.shambavi.thericecompany.databinding.ItemFilterSectionBinding

class FilterAdapter : ListAdapter<FilterSection, FilterAdapter.FilterViewHolder>(FilterDiffCallback()) {
    private val selectedFilters = mutableMapOf<String, MutableSet<String>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = ItemFilterSectionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getSelectedFilters(): Map<String, Set<String>> = selectedFilters.toMap()
    fun clearFilters(){
        selectedFilters.clear()
        notifyDataSetChanged()
    }

    inner class FilterViewHolder(private val binding: ItemFilterSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(section: FilterSection) {
            binding.tvTitle.text = section.title
            binding.rvOptions.apply {
                layoutManager = FlexboxLayoutManager(context).apply {
                    flexDirection = FlexDirection.ROW
                    flexWrap = FlexWrap.WRAP
                }
                adapter = FilterOptionsAdapter(section.options) { option, isSelected ->
                    if (isSelected) {
                        selectedFilters.getOrPut(section.title) { mutableSetOf() }.add(option)
                    } else {
                        selectedFilters[section.title]?.remove(option)
                    }
                }
            }
        }
    }
}
