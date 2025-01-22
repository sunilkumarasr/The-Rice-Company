package com.shambavi.thericecompany.categories

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.LayoutCategoryListItemWithHeaderBinding
import com.shambavi.thericecompany.home.CategoryAdapter
import com.shambavi.thericecompany.products.CategoryProductsActivity
import com.shambavi.thericecompany.products.ProductDetailsActivity

class AllCategoryAdapter(val ctx:Context): RecyclerView.Adapter<AllCategoryAdapter.AllCategoryViewHolder>() {

lateinit var layoutManager:LinearLayoutManager
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
        return 10
    }

    override fun onBindViewHolder(holder: AllCategoryViewHolder, position: Int) {
        layoutManager= LinearLayoutManager(ctx,LinearLayoutManager.HORIZONTAL,false)

        holder.binding.recyclerTopCatgories.layoutManager=layoutManager
        holder.binding.recyclerTopCatgories.adapter=CategoryAdapter()
        holder.itemView.setOnClickListener {
            val ctx=holder.binding.recyclerTopCatgories.context
            ctx.startActivity(Intent(ctx, CategoryProductsActivity::class.java))
        }


    }
}