package com.shambavi.thericecompany.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gadiwalaUser.Models.Category
import com.gadiwalaUser.Models.Product
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivitySplashBinding
import com.shambavi.thericecompany.databinding.LayoutHomeProductItemBinding
import com.shambavi.thericecompany.products.ProductDetailsActivity
import com.shambavi.thericecompany.utils.Utils

class ProductsAdapter: RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    var productList:ArrayList<Product> = arrayListOf()

    class ProductViewHolder(val binding: LayoutHomeProductItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val binding=LayoutHomeProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       // val view=LayoutInflater.from(parent.context).inflate(R.layout.layout_home_product_item,parent,false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        var obj=productList.get(position)
        holder.binding.txtProductName.text="${obj.title}"
        holder.binding.txtMrp.text="${Utils.RUPEE_SYMBOL} ${obj.mrpPrice}"
        holder.binding.txtOff.text="${Utils.RUPEE_SYMBOL} ${obj.marketPrice}"
        holder.binding.txtOurPrice.text="${Utils.RUPEE_SYMBOL} ${obj.ourPrice}"
        holder.binding.txtMarketPrice.text="${Utils.RUPEE_SYMBOL} ${obj.marketPrice}"
        holder.binding.txtProductCategory.text="${obj.categoryId}"
        holder.binding.txtProductName.setOnClickListener {
            val ctx=holder.binding.txtProductName.context
            val intent=Intent(ctx,ProductDetailsActivity::class.java)
            intent.putExtra("product_id",productList.get(position).id)

            ctx.startActivity(intent)        }
        holder.binding.root.setOnClickListener {
            val ctx=holder.binding.txtProductName.context
            val intent=Intent(ctx,ProductDetailsActivity::class.java)
            intent.putExtra("product_id",productList.get(position).id)

            ctx.startActivity(intent)

        }
    }
}