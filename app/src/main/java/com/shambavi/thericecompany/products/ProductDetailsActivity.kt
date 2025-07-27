package com.shambavi.thericecompany.products

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bookiron.itpark.utils.MyPref
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.gadiwalaUser.Models.MainResponse
import com.gadiwalaUser.Models.ProductDetailsDataMinRes
import com.gadiwalaUser.services.DataManager
import com.gadiwalaUser.services.DataManager.Companion.ROOT_URL
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Activitys.PrivacyPolicyActivity
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.cart.CheckOutActivity
import com.shambavi.thericecompany.databinding.ActivityProductDetailsBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityProductDetailsBinding
    var product_id = ""
    var attribute_id = ""
    var user_id = ""
    var cart_id = ""
    var quantity = 0
    val imageList = ArrayList<SlideModel>()
    var chipList: ArrayList<ChipPrices>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        user_id = MyPref.getUser(applicationContext)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(
            this,
            ContextCompat.getColor(this, R.color.colorPrimary),
            false
        )

        product_id = intent.getStringExtra("product_id").toString()
        Log.e("product_id", "product_id $product_id")
        getProductDetails()
        binding.txtMrpPrice.paintFlags =
            binding.txtMrpPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        binding.txtViewReports.paintFlags =
            binding.txtViewReports.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.txtViewDetails.setOnClickListener {
            val intent = Intent(applicationContext, PrivacyPolicyActivity::class.java)
            intent.putExtra("isOtherDetails", "isOtherDetails")
            startActivity(intent)
        }
        binding.txtViewReports.setOnClickListener {
            val intent = Intent(applicationContext, PrivacyPolicyActivity::class.java)
            intent.putExtra("isOtherDetails", "isOtherDetails")
            startActivity(intent)
        }

        binding.backButton.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        binding.btnAddToCart.setOnClickListener {
            if (attribute_id.isEmpty()) {
                Utils.showMessage("Please select Attribution value", applicationContext)
                return@setOnClickListener
            }
            addToCart()

        }
        binding.btnMinus.setOnClickListener {
            quantity = quantity - 1;
            if (quantity == 0)
                deleteCart()
            else
                updateCart()
        }
        binding.btnPlus.setOnClickListener {
            quantity = quantity + 1;
            updateCart()
        }

        binding.btnViewCart.setOnClickListener {
            startActivityForResult(Intent(applicationContext, CheckOutActivity::class.java), 200)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getProductDetails()
    }

    private fun deleteCart() {

        val dialog = CustomDialog(this@ProductDetailsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@ProductDetailsActivity, false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                    //model?.Message?.let { Utils.showMessage(it,requireActivity()) }

                    binding.btnAddToCart.visibility = View.VISIBLE
                    binding.lnrViewcart.visibility = View.GONE


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
        dataManager.deleteProduct(otpCallback, user_id, cart_id)

    }

    fun setData() {
        val lp = FlexboxLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            FlexboxLayout.LayoutParams.WRAP_CONTENT
        )
        lp.marginStart = 10
        lp.marginEnd = 10
        binding.flexboxLayout.removeAllViews()

        binding.btnAddToCart.visibility = View.GONE
        for (chipText in chipList!!) {
            val chip = Chip(this)
            chip.text = chipText.price
            chip.id = Integer.parseInt(chipText.id)
            chip.isChecked = chipText.isSelected
            // chip.layoutParams=lp

            if (!chipText.isSelected) {
                chip.chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )


                )
                chip.setTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.black
                        )
                    )


                )

            } else {
                chip.chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.green
                    )
                )
                chip.setTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.white
                        )
                    )
                )
                if (cart_id.isEmpty())
                    binding.btnAddToCart.visibility = View.VISIBLE
            }

            chip.setOnClickListener {
                updateChips(it.id.toString())
            }

            binding.flexboxLayout.addView(chip)
        }
    }

    private fun updateChips(id: String) {
        chipList!!.forEach {
            if (it.id == id) {
                it.isSelected = true
                attribute_id = it.id
            } else {
                it.isSelected = false
            }
        }
        setData()

    }

    data class ChipPrices(val price: String, val id: String, var isSelected: Boolean = false)

    fun getProductDetails() {

        val dialog = CustomDialog(this@ProductDetailsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@ProductDetailsActivity, false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<ProductDetailsDataMinRes> {
            override fun onResponse(
                call: Call<ProductDetailsDataMinRes>,
                response: Response<ProductDetailsDataMinRes>
            ) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: ProductDetailsDataMinRes? = response.body()

                    // Handle the response

                    model?.message?.let { Utils.showMessage(it, applicationContext) }

                    if (model?.status == true) {
                        val productDetails = model.data?.productDetails
                        binding.txtProductName.text = "${productDetails!!.title}"
                        binding.txtMrpPrice.text =
                            "${Utils.RUPEE_SYMBOL}${productDetails!!.mrpPrice}"
                        binding.txtOurPrice.text =
                            "${Utils.RUPEE_SYMBOL}${productDetails!!.ourPrice}"
                        binding.txtMarketPrice.text =
                            "${Utils.RUPEE_SYMBOL}${productDetails!!.marketPrice}"
                        binding.webviewDescription.loadData(
                            "${productDetails!!.descriptions}"+
                            "Specification: \n${productDetails!!.specifications}",
                            "text/html",
                            "utf-8"
                        )
                        binding.txtProductDescription.text = "${productDetails!!.descriptions}"

                        if(productDetails.broucher!!.isEmpty())
                            binding.txtViewReports.visibility=View.GONE
                        else
                            binding.txtViewReports.visibility=View.VISIBLE
                            binding.txtViewReports.setOnClickListener {

                                val pdfUrl = productDetails.broucher // Assuming this is the full URL to the PDF
                                if (pdfUrl!!.startsWith("http://") || pdfUrl.startsWith("https://")) {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW)
                                        intent.setDataAndType(Uri.parse(pdfUrl), "application/pdf")
                                        // Add FLAG_ACTIVITY_NO_HISTORY so when the user presses back,
                                        // they return to your app, not the browser's history stack for this PDF.
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                        // Optional: Grant read URI permission if needed by some viewers,
                                        // though typically not required for public http/https URLs.
                                        // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                                        // Verify that an app exists to receive the intent
                                        if (intent.resolveActivity(packageManager) != null) {
                                            startActivity(intent)
                                        } else {
                                            // No app found to view PDF
                                            Utils.showMessage("No application found to view PDF files.",
                                                this@ProductDetailsActivity,)
                                            // Optionally, you could try opening it in a browser directly
                                            // val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
                                            // startActivity(browserIntent)
                                        }
                                    } catch (e: ActivityNotFoundException) {
                                        // Should be caught by resolveActivity check, but good for safety
                                        Utils.showMessage(

                                            "Error opening PDF: Viewer not found.",
                                            this@ProductDetailsActivity,)
                                        Log.e("PDFViewError", "ActivityNotFoundException: ${e.message}")
                                    } catch (e: Exception) {

                                        Log.e("PDFViewError", "Exception: ${e.message}")
                                    }
                                } else {

                                }
                            }
                        if (model.data!!.productDetails!!.user_rating!!.isEmpty() || model.data!!.productDetails!!.user_rating!!.trim()
                                .equals("0")
                        )
                            binding.lnrRating.visibility = View.INVISIBLE
                        else {
                            binding.lnrRating.visibility = View.VISIBLE
                            binding.txtRatingNo.setText("( ${model.data!!.productDetails!!.user_count} )")

                            binding.txtRating.setText("${model.data!!.productDetails!!.user_rating}")
                        }
                        chipList!!.clear()
                        model.data!!.productAttribute.forEach {
                            chipList!!.add(
                                ChipPrices(
                                    it.weight.toString(),
                                    it.id.toString(),
                                    false
                                )
                            )
                        }
                        if (model.data!!.latestAttribute != null) {
                            attribute_id = model.data!!.latestAttribute!!.id.toString()
                            updateChips(attribute_id)
                        }

                        imageList.clear()
                        imageList.add(
                            SlideModel(
                                ROOT_URL + "" + productDetails.image,
                                ScaleTypes.CENTER_INSIDE
                            )
                        )// for one image

                        model.data!!.productImages.forEach {
                            imageList.add(
                                SlideModel(
                                    ROOT_URL + "" + it.additionalImage,
                                    ScaleTypes.CENTER_INSIDE
                                )
                            )// for one image

                        }
                        model.data!!.cartDetails.let {

                            if (it!!.cartId!!.isEmpty()) {

                                binding.lnrViewcart.visibility = View.GONE
                                binding.btnAddToCart.visibility = View.VISIBLE
                            } else {
                                binding.lnrViewcart.visibility = View.VISIBLE
                                binding.btnAddToCart.visibility = View.GONE
                                binding.tvQuantity.text = "${it.quantity}"
                                if (it.quantity != null)
                                    quantity = Integer.parseInt(it.quantity)
                                cart_id = it.cartId.toString()
                            }
                        }

                        binding.imageSlider.setImageList(imageList)
                        setData()

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
        dataManager.productDetails(otpCallback, product_id, user_id)
    }

    fun addToCart() {

        val dialog = CustomDialog(this@ProductDetailsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@ProductDetailsActivity, false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                    model?.Message?.let { Utils.showMessage(it, applicationContext) }

                    getProductDetails()
                    if (model!!.Status!!) {
                        binding.btnAddToCart.visibility = View.GONE
                        binding.lnrViewcart.visibility = View.VISIBLE
                    }

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
        dataManager.addCart(otpCallback, user_id, product_id, attribute_id)
    }


    fun updateCart() {

        val dialog = CustomDialog(this@ProductDetailsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@ProductDetailsActivity, false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                    model?.Message?.let { Utils.showMessage(it, applicationContext) }

                    if (model!!.Status!!) {
                        binding.tvQuantity.text = "$quantity"
                    }

                    //getProductDetails()
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
        dataManager.updateCart(otpCallback, user_id, cart_id, quantity.toString())
    }
}