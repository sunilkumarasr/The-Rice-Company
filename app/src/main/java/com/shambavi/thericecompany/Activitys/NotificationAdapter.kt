package com.shambavi.thericecompany.Activitys

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gadiwalaUser.Models.Notification
import com.shambavi.thericecompany.R

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.NotificationHolder>(){
    var list: ArrayList<Notification> = ArrayList<Notification>()

    // Keep track of expanded states for each item
    private val expandedStates = mutableMapOf<Int, Boolean>()

    fun setLists(lists: ArrayList<Notification>?) {
        list.clear()
        if (lists != null) {
            list.addAll(lists)
            // Reset expanded states when new list is set
            expandedStates.clear()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_notification_item, parent, false)
        return NotificationHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val notificationItem = list[position]
        holder.txt_title.text = notificationItem.title
        holder.txt_descrption.movementMethod = LinkMovementMethod.getInstance()

        val fullDescription = fromHtml(notificationItem.body)
        holder.txt_descrption.text = fullDescription

        // Check if the item is expanded or not
        val isExpanded = expandedStates[position] ?: false

        holder.txt_descrption.post { // Wait for the layout pass to get line count
            if (holder.txt_descrption.lineCount > 2) {
                holder.txt_read_more.visibility = View.VISIBLE
                if (isExpanded) {
                    holder.txt_descrption.maxLines = Int.MAX_VALUE
                    holder.txt_read_more.text = "Read Less"
                } else {
                    holder.txt_descrption.maxLines = 2
                    holder.txt_read_more.text = "Read More"
                }
            } else {
                holder.txt_read_more.visibility = View.GONE
                holder.txt_descrption.maxLines = Int.MAX_VALUE // Show all lines if less than or equal to 2
            }
        }

        holder.txt_read_more.setOnClickListener {
            val currentExpandedState = expandedStates[position] ?: false
            expandedStates[position] = !currentExpandedState
            notifyItemChanged(position) // Refresh the item to apply changes
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class NotificationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_title: TextView = itemView.findViewById<TextView>(R.id.txt_title)
        var txt_descrption: TextView = itemView.findViewById<TextView>(R.id.txt_descrption)
        var txt_read_more: TextView = itemView.findViewById<TextView>(R.id.txt_read_more)
    }
}

fun fromHtml(source: String?): Spanned? {
    if (source == null) return null
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(source)
    }
}