package com.shambavi.thericecompany.products

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gadiwalaUser.Models.CategoryMainRes
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityCategoryProductsBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryProductsActivity : AppCompatActivity() {
    lateinit var binding:ActivityCategoryProductsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCategoryProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            finish()
        }
        getCategories()
    }
    fun getCategories()
    {

        val dialog= CustomDialog(this@CategoryProductsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@CategoryProductsActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<CategoryMainRes> {
            override fun onResponse(call: Call<CategoryMainRes>, response: Response<CategoryMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: CategoryMainRes? = response.body()

                    // Handle the response

                    model?.message?.let { Utils.showMessage(it,applicationContext) }

                    if(model?.status == true)
                    {
                        if(model.categories.size>0) {

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