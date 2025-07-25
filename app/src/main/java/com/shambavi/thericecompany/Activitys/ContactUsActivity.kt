package com.shambavi.thericecompany.Activitys

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gadiwalaUser.Models.ContactDetails
import com.gadiwalaUser.Models.ContactDetailsMain
import com.gadiwalaUser.Models.PrivacyDataMainRes
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Config.ViewController
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
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

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
        val otpCallback = object : Callback<ContactDetailsMain> {
            override fun onResponse(call: Call<ContactDetailsMain>, response: Response<ContactDetailsMain>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: ContactDetailsMain? = response.body()

                    if(model?.status == true){
                        binding.txtAddress.text = model.date.get(0).address
                        binding.txtPhone1.text = model.date.get(0).phone
                        binding.txtPhone2.text = model.date.get(0).phone2
                        binding.txtEmail.text = model.date.get(0).email
                    }

                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")
                    finish()
                }
            }

            override fun onFailure(call: Call<ContactDetailsMain>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.contactDetails(otpCallback)
    }
}