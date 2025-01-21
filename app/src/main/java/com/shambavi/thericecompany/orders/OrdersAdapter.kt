package com.shambavi.thericecompany.orders

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.shambavi.thericecompany.databinding.LayoutOrdersItemBinding

class OrdersAdapter (
    private val orders: List<Order>,
    private val onOrderClick: (Order) -> Unit,
    private val onRateClick: (Order) -> Unit
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(private val binding: LayoutOrdersItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            with(binding) {
                // Load product image using Glide or similar library
                Glide.with(productImage)
                    .load(order.productImage)
                    .into(productImage)

                // Set order status and style
                orderStatusText.text = when (order.orderStatus) {
                    OrderStatusEnum.DELIVERY_EXPECTED -> "Delivery expected by ${order.deliveryDate}"
                    OrderStatusEnum.CANCELLED -> "Cancelled on ${order.deliveryDate}"
                    OrderStatusEnum.DELIVERED -> "Delivered on ${order.deliveryDate}"
                    OrderStatusEnum.REFUNDED -> "Refund on ${order.deliveryDate}"
                }

                productNameText.text = order.productName

                // Handle rating visibility
                if (order.orderStatus == OrderStatusEnum.DELIVERED) {
                    if (order.rating != null) {
                        ratingBar.visibility = View.VISIBLE
                        ratingBar.rating = order.rating
                        rateProductText.visibility = View.GONE
                    } else {
                        ratingBar.visibility = View.GONE
                        rateProductText.visibility = View.VISIBLE
                        rateProductText.setOnClickListener { onRateClick(order) }
                    }
                } else {
                    ratingBar.visibility = View.GONE
                    rateProductText.visibility = View.GONE
                }

                // Set click listener
                root.setOnClickListener { onOrderClick(order) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = LayoutOrdersItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
        holder.itemView.setOnClickListener {
          val intent=  Intent(holder.itemView.context,OrderDetailsActivity::class.java).apply {
                putExtra("order_id","1")

            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = orders.size
}
