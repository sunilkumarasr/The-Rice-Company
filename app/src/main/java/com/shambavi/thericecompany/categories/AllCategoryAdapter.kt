package com.shambavi.thericecompany.categories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.home.CategoryAdapter

class AllCategoryAdapter(val ctx:Context): RecyclerView.Adapter<AllCategoryAdapter.AllCategoryViewHolder>() {

lateinit var layoutManager:LinearLayoutManager
init {
    layoutManager= LinearLayoutManager(ctx,LinearLayoutManager.HORIZONTAL,false)
}
    class AllCategoryViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var recyclerView:RecyclerView=view.findViewById(R.id.recycler_top_catgories)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCategoryViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_category_list_item_with_header,parent,false)

        return AllCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: AllCategoryViewHolder, position: Int) {
        layoutManager= LinearLayoutManager(ctx,LinearLayoutManager.HORIZONTAL,false)

        holder.recyclerView.layoutManager=layoutManager
        holder.recyclerView.adapter=CategoryAdapter()

    }
}