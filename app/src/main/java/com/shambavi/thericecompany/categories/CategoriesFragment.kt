package com.shambavi.thericecompany.categories

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gadiwalaUser.Models.CategoryMainRes
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Activitys.SearchActivity
import com.shambavi.thericecompany.databinding.FragmentProductsBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        binding.linearSearch.setOnClickListener {
            startActivityForResult(Intent(requireActivity(), SearchActivity::class.java),100)
        }
        getCategories()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getCategories()
    }

    private fun init() {
        allCategoryAdapter= context?.let { AllCategoryAdapter(it) }!!
        binding.recyclerAllCategories.adapter=allCategoryAdapter
    }

    fun getCategories()
    {

        val dialog= CustomDialog(requireActivity())
        // Obtain the DataManager instance
        dialog.showDialog(requireActivity(),false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<CategoryMainRes> {
            override fun onResponse(call: Call<CategoryMainRes>, response: Response<CategoryMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: CategoryMainRes? = response.body()

                    // Handle the response

                  //  model?.message?.let { Utils.showMessage(it, activity!!) }

                    if(model?.status == true)
                    {
                        if(model.categories.size>0) {

                            allCategoryAdapter.categoryList.clear()
                            allCategoryAdapter.categoryList.addAll(model.categories)
                            allCategoryAdapter.notifyDataSetChanged()
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
        dataManager.getcategorySubCat(otpCallback)
    }
}