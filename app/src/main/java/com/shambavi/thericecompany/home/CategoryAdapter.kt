package com.shambavi.thericecompany.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivitySplashBinding
import com.shambavi.thericecompany.databinding.LayoutCategoryListItemWithHeaderBinding
import com.shambavi.thericecompany.databinding.LayoutHomeTopCategoriesBinding
import com.shambavi.thericecompany.products.CategoryProductsActivity

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(val binding: LayoutHomeTopCategoriesBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_home_top_categories,parent,false)
        val binding= LayoutHomeTopCategoriesBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.imgProduct.setOnClickListener {
            holder.binding.txtProductName.performClick()
        }
        holder.binding.txtProductName.setOnClickListener {
            val ctx=holder.binding.txtProductName.context
            ctx.startActivity(Intent(ctx, CategoryProductsActivity::class.java))
        }

    }
}