package com.shambavi.thericecompany.Activitys

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.MainResponse
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityEnquiryFormBinding
import com.shambavi.thericecompany.databinding.ActivityTermsAndConditionsBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnquiryFormActivity : AppCompatActivity() {
    var name=""
    var email=""
    var phone=""
    var subject=""
    var message=""
    val binding: ActivityEnquiryFormBinding by lazy {
        ActivityEnquiryFormBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        binding.header.txtTitle.setText("Enquiry Form")
        binding.lnrCancel.setOnClickListener {
            finish()
        }

        binding.header.imgBack.setOnClickListener {
            finish()
        }
        binding.lnrSend.setOnClickListener {
            subject=binding.editSubject.text.toString()
            message=binding.editMessage.text.toString()
            if(subject.isEmpty()||message.isEmpty())
            {
                Utils.showMessage("Please fill details",applicationContext)
                return@setOnClickListener
            }
            name= MyPref.getName(applicationContext).toString()
            phone= MyPref.getMobile(applicationContext).toString()
            email= MyPref.getEmail(applicationContext).toString()
            callData()
        }

    }
    fun callData()
    {

        val dialog= CustomDialog(applicationContext)
        // Obtain the DataManager instance
        dialog.showDialog(this@EnquiryFormActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {

                    val model: MainResponse? = response.body()
                    model!!.Message?.let { Utils.showMessage(it,applicationContext) }
                    finish()
                    // Handle the response

                   /* model?.message?.let { Utils.showMessage(it,applicationContext) }

                    if(model?.status == true)
                    {
                        if(model.data.size>0) {


                            return
                        }
                        finish()
                    }
                    println("OTP Sent successfully: ${model?.message}")*/
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")
                    finish()
                }
            }

            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.submitEnquiery(otpCallback,name,email,phone,subject,message)
    }
}