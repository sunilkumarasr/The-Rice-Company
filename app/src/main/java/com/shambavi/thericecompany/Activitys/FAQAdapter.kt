package com.shambavi.thericecompany.Activitys

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gadiwalaUser.Models.FAQs
import com.shambavi.thericecompany.R

class FAQAdapter(private val faqList: List<FAQs>) :
    RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {

    class FAQViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.faqTitleTextView)
        val secondLineTextView: TextView = itemView.findViewById(R.id.secondLineTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.faqDescriptionTextView)
        val expandIconImageView: ImageView = itemView.findViewById(R.id.expandIconImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_faq_item, parent, false)
        return FAQViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val currentItem = faqList[position]
        holder.titleTextView.text = currentItem.question
        holder.descriptionTextView.text = currentItem.answer

        // Expand/Collapse functionality
        holder.expandIconImageView.setOnClickListener {
            val isVisible = holder.descriptionTextView.visibility == View.VISIBLE
            holder.descriptionTextView.visibility = if (isVisible) View.GONE else View.VISIBLE
            if(isVisible)
            {
                holder.expandIconImageView.rotation=-90f
            }else
            {
                holder.expandIconImageView.rotation=90f
            }
           // holder.expandIconImageView.setImageResource(if (isVisible) R.drawable.ic_expand_more else R.drawable.ic_expand_less)
        }
    }

    override fun getItemCount() = faqList.size
}