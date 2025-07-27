package com.shambavi.thericecompany.filters

import com.gadiwalaUser.Models.PriceRange

data class FilterSection(val title: String,
                         val options: ArrayList<PriceRange>
)
