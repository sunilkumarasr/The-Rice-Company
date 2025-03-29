package com.shambavi.thericecompany.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gadiwalaUser.Models.CartModel
import com.gadiwalaUser.services.DataManager.Companion.ROOT_URL
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.LayoutCartItemBinding
import com.shambavi.thericecompany.listeners.ProductListener
import com.shambavi.thericecompany.utils.Utils.Companion.RUPEE_SYMBOL

class CartAdapter: RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    var cartList:ArrayList<CartModel> = arrayListOf()
    lateinit var productListener: ProductListener

    fun setListener(productListener: ProductListener)
    {
       this .productListener=productListener
    }
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
var cart=cartList.get(position)
        holder.binding.tvProductName.setText("${cart.title}")
        holder.binding.tvProductType.setText("${cart.categoryIdName}")
        holder.binding.tvQuantity.setText("${cart.quantity}")
        holder.binding.tvOriginalPrice.setText("${RUPEE_SYMBOL} ${cart.mrpPrice}")
        holder.binding.tvPrice.setText("${RUPEE_SYMBOL} ${cart.ourPrice}")
        holder.binding.lnrRemove.setOnClickListener {
            cart.cartId?.let { it1 -> productListener.deleteProduct(it1) }
        }
        holder.binding.btnMinus.setOnClickListener {
            var qnty=Integer.parseInt(cart.quantity)
            holder.binding.tvQuantity.text="$qnty"
            cart.cartId?.let { it1 -> productListener.updateProduct(it1,qnty-1) }
        }
        holder.binding.btnPlus.setOnClickListener {
            var qnty=Integer.parseInt(cart.quantity)
            holder.binding.tvQuantity.text="$qnty"
            cart.cartId?.let { it1 -> productListener.updateProduct(it1,qnty+1) }
        }

        Glide.with(holder.binding.imgProduct.context).load(ROOT_URL+cartList.get(position).image).placeholder(R.drawable.item1).into(holder.binding.imgProduct)


    }
}