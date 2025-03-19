package com.shambavi.thericecompany.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.gadiwalaUser.Models.CategoryMainRes
import com.gadiwalaUser.Models.FAQsMainRes
import com.gadiwalaUser.Models.ProductMainRes
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Activitys.NotificationsActivity
import com.shambavi.thericecompany.Activitys.SearchActivity
import com.shambavi.thericecompany.databinding.FragmentHomeBinding
import com.shambavi.thericecompany.products.AllProductsActivity
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        getCategories()
        getProducts()
        getTopSellProducts()
    }

    fun getCategories()
    {

        val dialog= CustomDialog(requireActivity())
        // Obtain the DataManager instance
        dialog.showDialog(activity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<CategoryMainRes> {
            override fun onResponse(call: Call<CategoryMainRes>, response: Response<CategoryMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: CategoryMainRes? = response.body()

                    // Handle the response

                    model?.message?.let { Utils.showMessage(it,requireActivity()) }

                    if(model?.status == true)
                    {
                        if(model.categories.size>0) {
                            categoryAdapter.categoryList.clear()
                            categoryAdapter.categoryList.addAll(model.categories)
                            categoryAdapter.notifyDataSetChanged()
                            return
                        }

                    }
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<CategoryMainRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.getcategory(otpCallback)
    }
    fun getProducts()
    {

        val dialog= CustomDialog(requireActivity())
        // Obtain the DataManager instance
        dialog.showDialog(activity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<ProductMainRes> {
            override fun onResponse(call: Call<ProductMainRes>, response: Response<ProductMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: ProductMainRes? = response.body()

                    // Handle the response

                    model?.message?.let { Utils.showMessage(it,requireActivity()) }

                    if(model?.status == true)
                    {
                        if(model.products.size>0) {
                            productsAdapter.productList.clear()
                            productsAdapter.productList.addAll(model.products)
                            productsAdapter.notifyDataSetChanged()
                            return
                        }

                    }
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<ProductMainRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.getProducts(otpCallback)
    }
    fun getTopSellProducts()
    {

        val dialog= CustomDialog(requireActivity())
        // Obtain the DataManager instance
        dialog.showDialog(activity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<ProductMainRes> {
            override fun onResponse(call: Call<ProductMainRes>, response: Response<ProductMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: ProductMainRes? = response.body()

                    // Handle the response

                    model?.message?.let { Utils.showMessage(it,requireActivity()) }

                    if(model?.status == true)
                    {
                        if(model.products.size>0) {
                            topSellingAdapter.productList.clear()
                            topSellingAdapter.productList.addAll(model.products)
                            topSellingAdapter.notifyDataSetChanged()
                            return
                        }

                    }
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<ProductMainRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.getTopSellProducts(otpCallback)
    }

}