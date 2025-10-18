package com.shambavi.thericecompany.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestOptions
import com.gadiwalaUser.Models.Category
import com.gadiwalaUser.services.DataManager.Companion.ROOT_URL
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.LayoutHomeTopCategoriesBinding
import com.shambavi.thericecompany.products.CategoryProductsActivity
import com.shambavi.thericecompany.utils.Utils.Companion.options


class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var categoryList:ArrayList<Category> = arrayListOf()
    class CategoryViewHolder(val binding: LayoutHomeTopCategoriesBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_home_top_categories,parent,false)
        val binding= LayoutHomeTopCategoriesBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.imgProduct.setOnClickListener {
            holder.binding.txtProductName.performClick()
        }
        holder.binding.txtProductName.setText("${categoryList.get(position).category}")


        Glide.with(holder.binding.imgProduct.context).load(ROOT_URL+categoryList.get(position).categoryImage).apply(options).into(holder.binding.imgProduct)
        holder.binding.txtProductName.setOnClickListener {
            val ctx=holder.binding.txtProductName.context
            val intent=Intent(ctx, CategoryProductsActivity::class.java)
            intent.putExtra("cat_id",categoryList.get(position).id)
            intent.putExtra("cat_name",categoryList.get(position).category)
            ctx.startActivity(intent)
        }

    }
}