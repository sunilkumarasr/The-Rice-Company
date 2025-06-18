package com.shambavi.thericecompany.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gadiwalaUser.Models.CartModel
import com.gadiwalaUser.Models.Product
import com.gadiwalaUser.services.DataManager.Companion.ROOT_URL
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.LayoutCartItemBinding
import com.shambavi.thericecompany.databinding.LayoutOrderProductItemBinding
import com.shambavi.thericecompany.listeners.ProductListener
import com.shambavi.thericecompany.utils.Utils.Companion.RUPEE_SYMBOL

class OrderProductAdapter: RecyclerView.Adapter<OrderProductAdapter.OrderProductViewHolder>() {

    var cartList:ArrayList<OrderProduct> = arrayListOf()
    lateinit var orderDetailsActivity: OrderDetailsActivity

    fun setListener(orderDetailsActivity: OrderDetailsActivity)
    {
       this .orderDetailsActivity=orderDetailsActivity
    }
    class OrderProductViewHolder(val binding: LayoutOrderProductItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderProductViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_home_top_categories,parent,false)
        val binding= LayoutOrderProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return OrderProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: OrderProductViewHolder, position: Int) {
    var cart=cartList.get(position)
        var rating=0
        if(cart.userRating!=null&&cart.userRating!!.isNotEmpty())
        rating=cart.userRating!!.toInt()

        if(rating>0)
            holder.binding.ratingBar.setIsIndicator(true)
        else {
            holder.binding.ratingBar.setIsIndicator(false)
            holder.binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                cart.productId?.let {
                    if(rating>0)
                    orderDetailsActivity.setRating(it,rating.toString())
                }
            }
        }



        holder.binding.ratingBar.rating= rating.toFloat()


        holder.binding.tvProductName.setText("${cart.productTitle}")
        holder.binding.tvProductType.setText("${cart.productId}")

        holder.binding.tvOriginalPrice.setText("${RUPEE_SYMBOL} ${cart.mrpPrice}")
        holder.binding.tvPrice.setText("${RUPEE_SYMBOL} ${cart.ourPrice}")

        Glide.with(holder.binding.imgProduct.context).load(ROOT_URL+cartList.get(position).productImage).placeholder(R.drawable.item1).into(holder.binding.imgProduct)


    }
}