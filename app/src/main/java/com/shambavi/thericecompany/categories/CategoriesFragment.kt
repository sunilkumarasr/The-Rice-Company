package com.shambavi.thericecompany.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shambavi.thericecompany.databinding.FragmentProductsBinding

class CategoriesFragment : Fragment() {


    private lateinit var binding: FragmentProductsBinding

    lateinit var allCategoryAdapter: AllCategoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {
        allCategoryAdapter= context?.let { AllCategoryAdapter(it) }!!
        binding.recyclerAllCategories.adapter=allCategoryAdapter
    }

}