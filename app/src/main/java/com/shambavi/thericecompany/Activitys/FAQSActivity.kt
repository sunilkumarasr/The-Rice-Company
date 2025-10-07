package com.shambavi.thericecompany.Activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.gadiwalaUser.Models.FAQsMainRes
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityFaqsactivityBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FAQSActivity : AppCompatActivity() {
    lateinit var binding:ActivityFaqsactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFaqsactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        binding.header.txtTitle.text="Faq`s"

        binding.header.imgBack.setOnClickListener {
            finish()
        }
        callData()
    }

    fun callData()
    {

        val dialog= CustomDialog(applicationContext)
        // Obtain the DataManager instance
        dialog.showDialog(this@FAQSActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<FAQsMainRes> {
            override fun onResponse(call: Call<FAQsMainRes>, response: Response<FAQsMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: FAQsMainRes? = response.body()

                    // Handle the response

                    model?.message?.let { Utils.showMessage(it,applicationContext) }

                    if(model?.status == true)
                    {
                        if(model.data.size>0) {

                            var faqadapter=FAQAdapter(model.data)
                            binding.recyclerFaqs.layoutManager= LinearLayoutManager(applicationContext)
                            binding.recyclerFaqs.adapter=faqadapter


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

            override fun onFailure(call: Call<FAQsMainRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.faqs(otpCallback)
    }
}