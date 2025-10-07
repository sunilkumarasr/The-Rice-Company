package com.shambavi.thericecompany.Activitys

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gadiwalaUser.Models.PrivacyDataMainRes
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityPrivacyPolicyBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrivacyPolicyActivity : AppCompatActivity() {

    var isOtherDetails=""
    val binding: ActivityPrivacyPolicyBinding by lazy {
        ActivityPrivacyPolicyBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        isOtherDetails=  intent.getStringExtra("isOtherDetails").toString()

        inits()

    }

    private fun inits() {
        if(isOtherDetails.isNotEmpty())
            binding.header.txtTitle.setText("")
        else
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Privacy Policy"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            callData()
        }

    }
    fun callData()
    {

        val dialog= CustomDialog(applicationContext)
        // Obtain the DataManager instance
        dialog.showDialog(this@PrivacyPolicyActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<PrivacyDataMainRes> {
            override fun onResponse(call: Call<PrivacyDataMainRes>, response: Response<PrivacyDataMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: PrivacyDataMainRes? = response.body()

                    // Handle the response

                    model?.message?.let { Utils.showMessage(it,applicationContext) }

                    if(model?.status == true)
                    {
                        if(model.data.size>0) {
                            model.data.get(0).description?.let {
                                binding.webview.loadData(
                                    it,
                                    "text/html",
                                    "utf-8"
                                )
                            }
                            return
                        }
                        finish()
                    }
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")
                    finish()
                }
            }

            override fun onFailure(call: Call<PrivacyDataMainRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        if(isOtherDetails.isEmpty())
        dataManager.privacyTermsData(otpCallback,"privacy-policy")
        else
        dataManager.privacyTermsData(otpCallback,"shipping-policy")
    }
}