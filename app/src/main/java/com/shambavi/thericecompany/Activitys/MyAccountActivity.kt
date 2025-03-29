package com.shambavi.thericecompany.Activitys

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.MainResponse
import com.gadiwalaUser.Models.UserDetailsMainRes
import com.gadiwalaUser.services.DataManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.Logins.LoginActivity
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityMyAccountBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyAccountActivity : AppCompatActivity() {

    val binding: ActivityMyAccountBinding by lazy {
        ActivityMyAccountBinding.inflate(layoutInflater)
    }
var user_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)
        user_id=MyPref.getUser(applicationContext)
        inits()
        getProfile()
        binding.lnrDelete.setOnClickListener {
            deleteProfile()
        }
    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "My Account"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            //myAccountApi()
        }

        binding.imgEditProfile.setOnClickListener {
            ProfileDialog()
        }

    }

    private fun ProfileDialog() {
        val bottomSheetDialog = BottomSheetDialog(this@MyAccountActivity)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_editprofile, null)
        bottomSheetDialog.setContentView(view)
        val linearCancel = view.findViewById<TextView>(R.id.linearCancel)
        val linearUpload = view.findViewById<TextView>(R.id.linearUpload)
        linearCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        linearUpload.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    fun getProfile()
    {

        val dialog= CustomDialog(this@MyAccountActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@MyAccountActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<UserDetailsMainRes> {
            override fun onResponse(call: Call<UserDetailsMainRes>, response: Response<UserDetailsMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: UserDetailsMainRes? = response.body()

                    // Handle the response

                    model?.message?.let { Utils.showMessage(it,applicationContext) }
                    model!!.data.forEach {
                        binding.txtName.text="${it.fullName}"
                        binding.txtPhone.text="${it.phone}"
                        binding.txtEmail.text="${it.email}"
                    }


                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<UserDetailsMainRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.userDetails(otpCallback, user_id  )
    }

    fun deleteProfile()
    {

        val dialog= CustomDialog(this@MyAccountActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@MyAccountActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                    model?.Message?.let { Utils.showMessage(it,applicationContext) }

                    if(model!!.Status==true)
                    {
                        MyPref.clear(applicationContext)
                        finish()
                        val intent=Intent(applicationContext,LoginActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
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
        dataManager.deleteAccount(otpCallback, user_id  )
    }
}