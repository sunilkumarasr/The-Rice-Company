package com.shambavi.thericecompany.orders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shambavi.thericecompany.R

class FilterDateAdapter(private val context: Context,
                        private val dates: List<DeliveryDate>,
                        private val onDateSelected: (DeliveryDate) -> Unit
) : RecyclerView.Adapter<FilterDateAdapter.DateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter_date, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val date = dates[position]
        holder.bind(date)
    }

    override fun getItemCount(): Int = dates.size

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.date_text)

        fun bind(date: DeliveryDate) {
            dateTextView.text = date.date

            if (date.isSelected) {
                dateTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
                dateTextView.background = ContextCompat.getDrawable(context, R.drawable.selected_date_background)
                dateTextView.setTypeface(null, android.graphics.Typeface.BOLD)
            } else {
                dateTextView.setTextColor(ContextCompat.getColor(context, R.color.gray))
                dateTextView.background = null
                dateTextView.setTypeface(null, android.graphics.Typeface.NORMAL)
            }

            itemView.setOnClickListener {
                // Update selection state
                dates.forEach { it.isSelected = false }
                date.isSelected = true
                onDateSelected(date)
                notifyDataSetChanged()
            }
        }
    }
}
