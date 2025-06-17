package com.shambavi.thericecompany.Activitys

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.MainResponse
import com.gadiwalaUser.Models.ProductMainRes
import com.gadiwalaUser.services.DataManager
import com.royalit.motherchoice.utils.NetWorkConection
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityNotificationsBinding
import com.shambavi.thericecompany.databinding.ActivitySearchBinding
import com.shambavi.thericecompany.home.ProductsAdapter
import com.shambavi.thericecompany.listeners.ProductListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity(), ProductListener {

    val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }
    lateinit var productsAdapter: ProductsAdapter
    var user_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)
        productsAdapter=ProductsAdapter()
        productsAdapter.setListener(this)
        binding.recyclerProducts.layoutManager=
            GridLayoutManager(applicationContext,2)
        binding.recyclerProducts.adapter=productsAdapter
        user_id=MyPref.getUser(applicationContext)

        inits()
        if(!NetWorkConection.isNEtworkConnected(this@SearchActivity))
        {
            AlertDialog.Builder(this@SearchActivity)
                .setTitle("Alert!")
                .setMessage("No Network available")
                .show()
        }

    }

    var search_key="key"
    private fun inits() {

        binding.header.imgBack.setOnClickListener { finish() }
        binding.editSearch.setOnEditorActionListener(object:TextWatcher,
            TextView.OnEditorActionListener {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
               var key=s.toString()
                if(key.length>2)
                {
                    search_key=key;
                    searchProducts(key)
                }else
                {
                    search_key=""
                }
            }

            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                TODO("Not yet implemented")
            }

        })

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{

        }

    }
    fun searchProducts(key:String)
    {

        val dialog= CustomDialog(this@SearchActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@SearchActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<ProductMainRes> {
            override fun onResponse(call: Call<ProductMainRes>, response: Response<ProductMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: ProductMainRes? = response.body()

                    // Handle the response

                    ////model?.message?.let { Utils.showMessage(it,requireActivity()) }

                    if(model?.status == true)
                    {
                        if(model.products.size>0) {
                            productsAdapter.productList.clear()
                            productsAdapter.productList.addAll(model.products)
                            productsAdapter.notifyDataSetChanged()

                            checkData()
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
        dataManager.searchProduct(otpCallback,user_id,key)
    }

    private fun checkData() {

        if(productsAdapter.itemCount>0)
        {
            binding.txtNoData.visibility= View.GONE
            binding.recyclerProducts.visibility= View.VISIBLE
        }else
        {
            binding.txtNoData.visibility= View.VISIBLE
            binding.recyclerProducts.visibility= View.GONE
        }
    }

    override fun addProduct(product_id: String, attribution_id: String) {


    }

    override fun deleteProduct(cart_id: String)  {

        val dialog= CustomDialog(this@SearchActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@SearchActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                    //model?.Message?.let { Utils.showMessage(it,requireActivity()) }

                    searchProducts(search_key)


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

        val dialog= CustomDialog(this@SearchActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@SearchActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                    //model?.message?.let { Utils.showMessage(it,requireActivity()) }


                    searchProducts(search_key)
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
}