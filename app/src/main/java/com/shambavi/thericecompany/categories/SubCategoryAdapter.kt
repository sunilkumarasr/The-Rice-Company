package com.shambavi.thericecompany.categories

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gadiwalaUser.Models.Category
import com.gadiwalaUser.Models.SubCategory
import com.gadiwalaUser.services.DataManager.Companion.ROOT_URL
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivitySplashBinding
import com.shambavi.thericecompany.databinding.LayoutCategoryListItemWithHeaderBinding
import com.shambavi.thericecompany.databinding.LayoutHomeTopCategoriesBinding
import com.shambavi.thericecompany.products.AllProductsActivity
import com.shambavi.thericecompany.products.CategoryProductsActivity

class SubCategoryAdapter: RecyclerView.Adapter<SubCategoryAdapter.CategoryViewHolder>() {

    var categoryList:ArrayList<SubCategory> = arrayListOf()
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
         val ctx=holder.binding.txtProductName.context

        holder.binding.imgProduct.setOnClickListener {
            holder.binding.txtProductName.performClick()
        }
        holder.binding.txtProductName.setText("${categoryList.get(position).subCategory}")
        Glide.with(holder.binding.imgProduct.context).load(ROOT_URL+categoryList.get(position).subCategoryImage).into(holder.binding.imgProduct)
        holder.binding.txtProductName.setOnClickListener {
            val intent=Intent(ctx, AllProductsActivity::class.java)
            intent.putExtra("sid",categoryList.get(position).id)
            ctx.startActivity(intent)
           // val ctx=holder.binding.txtProductName.context
            //ctx.startActivity(Intent(ctx, CategoryProductsActivity::class.java))
        }

    }
}