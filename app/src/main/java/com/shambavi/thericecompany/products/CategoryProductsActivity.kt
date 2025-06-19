package com.shambavi.thericecompany.products

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.CategoryMainRes
import com.gadiwalaUser.Models.MainResponse
import com.gadiwalaUser.Models.ProductMainRes
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Activitys.SearchActivity
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.categories.AllCategoryAdapter
import com.shambavi.thericecompany.databinding.ActivityCategoryProductsBinding
import com.shambavi.thericecompany.home.ProductsAdapter
import com.shambavi.thericecompany.listeners.ProductListener
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryProductsActivity : AppCompatActivity(),ProductListener {
    lateinit var binding:ActivityCategoryProductsBinding
    lateinit var productsAdapter: ProductsAdapter
    lateinit var recycler_all_products: RecyclerView
    var user_id=""
    var cat_id=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCategoryProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        cat_id=intent.getStringExtra("cat_id").toString()
        user_id= MyPref.getUser(applicationContext)
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.linearSearch.setOnClickListener {
            startActivityForResult(Intent(this@CategoryProductsActivity, SearchActivity::class.java),100)
        }
        recycler_all_products=findViewById(R.id.recycler_all_products)
        productsAdapter=ProductsAdapter()
        recycler_all_products.adapter=productsAdapter
        productsAdapter.setListener(this)
        getProducts()
    }
    fun getProducts()
    {

        val dialog= CustomDialog(this@CategoryProductsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@CategoryProductsActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<ProductMainRes> {
            override fun onResponse(call: Call<ProductMainRes>, response: Response<ProductMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: ProductMainRes? = response.body()

                    // Handle the response



                    if(model?.status == true)
                    {
                        if(model.products.size>0) {
                            productsAdapter.productList.clear()
                            productsAdapter.productList.addAll(model.products)
                            productsAdapter.notifyDataSetChanged()

                        }else
                        {
                            model?.message?.let { Utils.showMessage(it,this@CategoryProductsActivity) }
                        }

                    }else
                    {
                        model?.message?.let { Utils.showMessage(it,this@CategoryProductsActivity) }
                    }
                    checkData()
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


            dataManager.getProductsByCat(otpCallback,cat_id,user_id)
    }
    private fun checkData() {

        if(productsAdapter.itemCount>0)
        {
            binding.txtNoData.visibility= View.GONE
            binding.recyclerAllProducts.visibility= View.VISIBLE
        }else
        {
            binding.txtNoData.visibility= View.VISIBLE
            binding.recyclerAllProducts.visibility= View.GONE
        }
    }

    override fun addProduct(product_id: String, attribution_id: String) {


        addCart(product_id,attribution_id)

    }

    override fun deleteProduct(cart_id: String)  {

        val dialog= CustomDialog(this@CategoryProductsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@CategoryProductsActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                    //model?.Message?.let { Utils.showMessage(it,requireActivity()) }

                    getProducts()


                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.deleteProduct(otpCallback, user_id,cart_id  )
    }

    override fun updateProduct(cart_id: String, qnty: Int) {

        val dialog= CustomDialog(this@CategoryProductsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@CategoryProductsActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                    //model?.message?.let { Utils.showMessage(it,requireActivity()) }


                    getProducts()
                    println("OTP Sent successfully: ${model?.Message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.updateCart(otpCallback,user_id ,cart_id,qnty.toString())
    }

    val startProductDetailsForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            getProducts()

        } else {
            // Log.d("MyActivity", "Result Canceled or Error: ${result.resultCode}")
        }
    }
    override fun onProductClick(productId: String) {
        val intent= Intent(this@CategoryProductsActivity, ProductDetailsActivity::class.java)
        intent.putExtra("product_id",productId)

        startProductDetailsForResult.launch(intent)
    }

    fun addCart(product_id: String, attribution_id: String)
    {

        val dialog= CustomDialog(this@CategoryProductsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@CategoryProductsActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                    //model?.message?.let { Utils.showMessage(it,requireActivity()) }


                    getProducts()
                    println("OTP Sent successfully: ${model?.Message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.addCart(otpCallback,user_id  ,product_id,attribution_id)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getProducts()
    }
}