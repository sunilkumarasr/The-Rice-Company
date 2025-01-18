package com.shambavi.thericecompany.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.shambavi.thericecompany.Activitys.NotificationsActivity
import com.shambavi.thericecompany.Activitys.SearchActivity
import com.shambavi.thericecompany.databinding.FragmentHomeBinding
import com.shambavi.thericecompany.products.AllProductsActivity

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
lateinit var categoryAdapter: CategoryAdapter
lateinit var productsAdapter: ProductsAdapter
lateinit var topSellingAdapter: ProductsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {

        categoryAdapter=CategoryAdapter()
        productsAdapter=ProductsAdapter()
        topSellingAdapter=ProductsAdapter()
        binding.recyclerTopCatgories.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerProducts.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerProductsTopSelling.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerTopCatgories.adapter=categoryAdapter
        binding.recyclerProducts.adapter=productsAdapter
        binding.recyclerProductsTopSelling.adapter=topSellingAdapter

        binding.imgNotification.setOnClickListener {
            startActivity(Intent(requireActivity(), NotificationsActivity::class.java))
        }
        binding.linearSearch.setOnClickListener {
            startActivity(Intent(requireActivity(), SearchActivity::class.java))
        }
        binding.txtSeeAllProducts.setOnClickListener {
            startActivity(Intent(context,AllProductsActivity::class.java))
        }

    }

}