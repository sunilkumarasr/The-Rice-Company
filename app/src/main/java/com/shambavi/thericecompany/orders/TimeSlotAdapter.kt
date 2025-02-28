package com.shambavi.thericecompany.orders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shambavi.thericecompany.R

class TimeSlotAdapter(
    private val context: Context,
    private val timeSlots: List<TimeSlot>,
    private val onTimeSlotSelected: (TimeSlot) -> Unit
) : RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter_date, parent, false)
        return TimeSlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        val timeSlot = timeSlots[position]
        holder.bind(timeSlot)
    }

    override fun getItemCount(): Int = timeSlots.size

    inner class TimeSlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timeSlotTextView: TextView = itemView.findViewById(R.id.date_text)

        fun bind(timeSlot: TimeSlot) {
            timeSlotTextView.text = timeSlot.timeRange

            if (timeSlot.isSelected) {
                timeSlotTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
                timeSlotTextView.background = ContextCompat.getDrawable(context, R.drawable.selected_date_background)
            } else {
                timeSlotTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
                timeSlotTextView.background = ContextCompat.getDrawable(context, R.drawable.unselected_date_background)
            }

            itemView.setOnClickListener {
                // Update selection state
                timeSlots.forEach { it.isSelected = false }
                timeSlot.isSelected = true
                onTimeSlotSelected(timeSlot)
                notifyDataSetChanged()
            }
        }
    }
}
