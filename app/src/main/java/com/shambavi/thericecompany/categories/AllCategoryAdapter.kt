package com.shambavi.thericecompany.categories

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.gadiwalaUser.Models.Category
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.LayoutCategoryListItemWithHeaderBinding
import com.shambavi.thericecompany.home.CategoryAdapter
import com.shambavi.thericecompany.products.CategoryProductsActivity
import com.shambavi.thericecompany.products.ProductDetailsActivity

class AllCategoryAdapter(val ctx:Context): RecyclerView.Adapter<AllCategoryAdapter.AllCategoryViewHolder>() {

lateinit var layoutManager:LinearLayoutManager
    var categoryList:ArrayList<Category> = arrayListOf()

init {
    layoutManager= LinearLayoutManager(ctx,LinearLayoutManager.HORIZONTAL,false)
}
    class AllCategoryViewHolder(val binding: LayoutCategoryListItemWithHeaderBinding): RecyclerView.ViewHolder(binding.root) {
        //var recyclerView:RecyclerView=view.findViewById(R.id.recycler_top_catgories)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCategoryViewHolder {

        val binding=LayoutCategoryListItemWithHeaderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AllCategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: AllCategoryViewHolder, position: Int) {
        layoutManager= GridLayoutManager(ctx,3)
val cateObj=categoryList.get(position)
        holder.binding.recyclerTopCatgories.layoutManager=layoutManager
        val catAdapter=SubCategoryAdapter()
        holder.binding.recyclerTopCatgories.adapter=catAdapter
        catAdapter.categoryList=cateObj.subCategoryList
        holder.binding.txtCategoryHeader.text="${cateObj.category}"
        holder.itemView.setOnClickListener {
            val ctx=holder.binding.recyclerTopCatgories.context
            ctx.startActivity(Intent(ctx, CategoryProductsActivity::class.java))
        }


    }
}