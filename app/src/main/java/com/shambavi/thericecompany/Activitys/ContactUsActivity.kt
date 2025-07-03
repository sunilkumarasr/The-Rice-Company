package com.shambavi.thericecompany.Activitys

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gadiwalaUser.Models.ContactDetails
import com.gadiwalaUser.Models.PrivacyDataMainRes
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityContactUsBinding
import com.shambavi.thericecompany.databinding.ActivityFaqsactivityBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUsActivity : AppCompatActivity() {
    lateinit var binding:ActivityContactUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding= ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.header.txtTitle.setText("Contact Us")
        binding.header.imgBack.setOnClickListener {
            finish()
        }
        callData()
    }
    fun callData()
    {

        val dialog= CustomDialog(applicationContext)
        // Obtain the DataManager instance
        dialog.showDialog(this@ContactUsActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<PrivacyDataMainRes> {
            override fun onResponse(call: Call<PrivacyDataMainRes>, response: Response<PrivacyDataMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: PrivacyDataMainRes? = response.body()

                    // Handle the response

                    model?.message?.let { Utils.showMessage(it,applicationContext) }
                    if(model?.status == true) {
                        if (model.data.size > 0) {
                            model.data.get(0).description?.let {
                                binding.webview.loadData(
                                    it,
                                    "text/html",
                                    "utf-8"
                                )
                            }
                            return
                        }
                    }
                    /*if(model?.status == true)
                    {
                        if(model.date.size>0) {

                            var text=""
                           model.date.forEach {
                               text="Address</br></br>" +it.address+"</br></br></br>Phone Number</br>"+it.phone+"</br>"+it.phone2+"</br></br></br>Email Address</br>"+it.email+""
                           }
                            binding.webview.loadData(text,"text/html","utf-8")

                            return
                        }
                        finish()
                    }*/
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

        // Call the sendOtp function in DataManager
        dataManager.privacyTermsData(otpCallback,"contact-us")
    }
}