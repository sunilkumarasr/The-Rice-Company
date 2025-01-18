package com.shambavi.thericecompany.products

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivitySplashBinding
import com.shambavi.thericecompany.home.ProductsAdapter

class AllProductsActivity : AppCompatActivity() {
    //lateinit var binding: ActivityllProductsBinding
    lateinit var productsAdapter: ProductsAdapter
    lateinit var recycler_all_products:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_products)
        recycler_all_products=findViewById(R.id.recycler_all_products)
        productsAdapter=ProductsAdapter()
        recycler_all_products.adapter=productsAdapter

    }
}