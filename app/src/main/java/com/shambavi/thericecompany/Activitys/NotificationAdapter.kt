package com.shambavi.thericecompany.Activitys

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gadiwalaUser.Models.Notification
import com.shambavi.thericecompany.R


class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.NotificationHolder?>(){
    var list: ArrayList<Notification> = ArrayList<Notification>()

    fun setLists(lists: ArrayList<Notification>?) {
        list.clear()
        list.addAll(lists!!)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_notification_item, parent, false)
        return NotificationHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {

        holder.txt_title.setText(list[position].title)
        holder.txt_descrption.setText(list[position].body)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class NotificationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_title: TextView = itemView.findViewById<TextView>(R.id.txt_title)
        var txt_descrption: TextView = itemView.findViewById<TextView>(R.id.txt_descrption)
    }
}