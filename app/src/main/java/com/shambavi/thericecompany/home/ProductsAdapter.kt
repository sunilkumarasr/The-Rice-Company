package com.shambavi.thericecompany.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivitySplashBinding
import com.shambavi.thericecompany.databinding.LayoutHomeProductItemBinding
import com.shambavi.thericecompany.products.ProductDetailsActivity

class ProductsAdapter: RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    class ProductViewHolder(val binding: LayoutHomeProductItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val binding=LayoutHomeProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       // val view=LayoutInflater.from(parent.context).inflate(R.layout.layout_home_product_item,parent,false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.binding.txtProductName.setOnClickListener {
            val ctx=holder.binding.txtProductName.context
            ctx.startActivity(Intent(ctx,ProductDetailsActivity::class.java))
        }
        holder.binding.root.setOnClickListener {
            val ctx=holder.binding.txtProductName.context
            ctx.startActivity(Intent(ctx,ProductDetailsActivity::class.java))

        }
    }
}