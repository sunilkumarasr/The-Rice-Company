package com.shambavi.thericecompany.products

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.ProductMainRes
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityAllProductsBinding
import com.shambavi.thericecompany.databinding.ActivitySplashBinding
import com.shambavi.thericecompany.filters.FilterBottomSheetFragment
import com.shambavi.thericecompany.home.ProductsAdapter
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllProductsActivity : AppCompatActivity(),FilterBottomSheetFragment.FilterCallback
{
    lateinit var binding: ActivityAllProductsBinding
    lateinit var productsAdapter: ProductsAdapter
    lateinit var recycler_all_products:RecyclerView
    var sid=""
    var sales=""
    var user_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // EdgeToEdge.enable(this);
        binding=ActivityAllProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sid=intent.getStringExtra("sid").toString()
        sales=intent.getStringExtra("sales").toString()
        user_id=MyPref.getUser(applicationContext)
        recycler_all_products=findViewById(R.id.recycler_all_products)
        productsAdapter=ProductsAdapter()
        recycler_all_products.adapter=productsAdapter
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.filterButton.setOnClickListener {
            showFilter()
        }
        getProducts()
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


    fun getProducts()
    {

        val dialog= CustomDialog(this@AllProductsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@AllProductsActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<ProductMainRes> {
            override fun onResponse(call: Call<ProductMainRes>, response: Response<ProductMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: ProductMainRes? = response.body()

                    // Handle the response

                    model?.message?.let { Utils.showMessage(it,this@AllProductsActivity) }

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
        if(sid.isEmpty())
            if(sales.isEmpty())
                dataManager.getProducts(otpCallback, user_id )
            else
                dataManager.getTopSellProducts(otpCallback,user_id)
        else
        dataManager.getProductsBySubCat(otpCallback,sid,user_id)
    }
}