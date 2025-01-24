package com.shambavi.thericecompany.products

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityAllProductsBinding
import com.shambavi.thericecompany.databinding.ActivitySplashBinding
import com.shambavi.thericecompany.filters.FilterBottomSheetFragment
import com.shambavi.thericecompany.home.ProductsAdapter

class AllProductsActivity : AppCompatActivity(),FilterBottomSheetFragment.FilterCallback
{
    lateinit var binding: ActivityAllProductsBinding
    lateinit var productsAdapter: ProductsAdapter
    lateinit var recycler_all_products:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // EdgeToEdge.enable(this);
        binding=ActivityAllProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recycler_all_products=findViewById(R.id.recycler_all_products)
        productsAdapter=ProductsAdapter()
        recycler_all_products.adapter=productsAdapter
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.filterButton.setOnClickListener {
            showFilter()
        }
    }
    private val filterLauncher = registerForActivityResult( ActivityResultContracts.StartActivityForResult() ) {
        result -> if (result.resultCode == RESULT_OK) {
        val filters = result.data?.getSerializableExtra("filters") as? Map<String, Set<String>> // Apply filters to your data
         } }
    private fun showFilter() {
        FilterBottomSheetFragment.newInstance() .show(supportFragmentManager, FilterBottomSheetFragment.TAG)
    }
            override fun onFiltersApplied(filters: Map<String, Set<String>>) {

            }



}