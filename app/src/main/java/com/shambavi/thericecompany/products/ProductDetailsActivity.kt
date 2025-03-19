package com.shambavi.thericecompany.products

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.gadiwalaUser.Models.ProductDetailsDataMinRes
import com.gadiwalaUser.services.DataManager
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.cart.CheckOutActivity
import com.shambavi.thericecompany.databinding.ActivityProductDetailsBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsActivity : AppCompatActivity() {
    lateinit var binding:ActivityProductDetailsBinding
    var product_id=""
    var  chipList:List<ChipPrices>? =null
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
            product_id=intent.getStringExtra("product_id").toString()
            Log.e("product_id","product_id $product_id")
            getProductDetails()
         chipList = listOf(ChipPrices("1kg","1",false),
            ChipPrices("2kg","2",false),
            ChipPrices("3kg","3",false),
            ChipPrices("4kg","4",false),
            ChipPrices("5kg","5",false),
            ChipPrices("6kg","6",false),
            ChipPrices("6kg","7",false),
            ChipPrices("6kg","8",false),
            ChipPrices("6kg","9",false),
            ChipPrices("6kg","10",false),
            ChipPrices("6kg","11",false),
            )

            setData()
            val imageList = ArrayList<SlideModel>() // Create image list

            imageList.add(SlideModel( R.drawable.item1, ScaleTypes.CENTER_CROP) )// for one image
            imageList.add(SlideModel( R.drawable.item2, ScaleTypes.CENTER_CROP) )// for one image
          binding. imageSlider.setImageList(imageList)


            binding.btnAddToCart.setOnClickListener {
                finish()
            }
            binding.backButton.setOnClickListener {
                finish()
            }

            binding.btnAddToCart.setOnClickListener {
                binding.btnAddToCart.visibility= View.GONE
                binding.lnrViewcart.visibility= View.VISIBLE
            }
            binding.btnMinus.setOnClickListener {
                binding.btnAddToCart.visibility= View.VISIBLE
                binding.lnrViewcart.visibility= View.GONE
            }

            binding.btnViewCart.setOnClickListener {
                startActivity(Intent(applicationContext,CheckOutActivity::class.java))
            }

        }

    fun setData()
    {
    val lp= FlexboxLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        FlexboxLayout.LayoutParams.WRAP_CONTENT
    )
        lp.marginStart=10
        lp.marginEnd=10
        binding.flexboxLayout.removeAllViews()
        for (chipText in chipList!!) {
            val chip = Chip(this)
            chip.text = chipText.price
            chip.id=Integer.parseInt(chipText.id)
            chip.isChecked=chipText.isSelected
           // chip.layoutParams=lp

            if(!chipText.isSelected) {
                chip.chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )


                )
                chip.setTextColor(ColorStateList.valueOf(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.black
                    )
                )


                )

            }
            else {
                chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.green))
                chip.setTextColor(ColorStateList.valueOf(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                ))
            }

            chip.setOnClickListener {
                updateChips(it.id)
            }

            binding.flexboxLayout.addView(chip)
        }
    }

    private fun updateChips(id: Int) {
        chipList!!.forEach {
            if(it.id==id.toString())
            {
                it.isSelected=true
            }else
            {
                it.isSelected=false
            }
        }
        setData()

    }

    data class ChipPrices(val price:String, val id:String, var isSelected:Boolean=false)

    fun getProductDetails()
    {

        val dialog= CustomDialog(this@ProductDetailsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@ProductDetailsActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<ProductDetailsDataMinRes> {
            override fun onResponse(call: Call<ProductDetailsDataMinRes>, response: Response<ProductDetailsDataMinRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: ProductDetailsDataMinRes? = response.body()

                    // Handle the response

                    model?.message?.let { Utils.showMessage(it,applicationContext) }

                    if(model?.status == true)
                    {



                    }
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<ProductDetailsDataMinRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.productDetails(otpCallback,product_id)
    }
}