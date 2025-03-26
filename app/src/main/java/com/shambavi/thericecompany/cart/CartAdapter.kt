package com.shambavi.thericecompany.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gadiwalaUser.Models.CartModel
import com.gadiwalaUser.services.DataManager.Companion.ROOT_URL
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.LayoutCartItemBinding
import com.shambavi.thericecompany.utils.Utils.Companion.RUPEE_SYMBOL

class CartAdapter: RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    var cartList:ArrayList<CartModel> = arrayListOf()
    class CartViewHolder(val binding: LayoutCartItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_home_top_categories,parent,false)
        val binding= LayoutCartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        holder.binding.tvProductName.setText("${cartList.get(position).title}")
        holder.binding.tvQuantity.setText("${cartList.get(position).quantity}")
        holder.binding.tvPrice.setText("${RUPEE_SYMBOL} ${cartList.get(position).ourPrice}")
        Glide.with(holder.binding.imgProduct.context).load(ROOT_URL+cartList.get(position).productId).into(holder.binding.imgProduct)


    }
}