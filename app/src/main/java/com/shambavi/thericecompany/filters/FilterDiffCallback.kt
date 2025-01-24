package com.shambavi.thericecompany.filters

import androidx.recyclerview.widget.DiffUtil

class FilterDiffCallback : DiffUtil.ItemCallback<FilterSection>() {
    override fun areItemsTheSame(oldItem: FilterSection, newItem: FilterSection) =
        oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: FilterSection, newItem: FilterSection) =
        oldItem == newItem
}
