package com.shambavi.thericecompany.Logins

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gadiwalaUser.Models.LoginResponse
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityRegisterBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    var mobile:String=""

    var address:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        inits()
    }

    private fun inits() {

        binding.linearLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
        binding.linearVerify.setOnClickListener {
            mobile=binding.editMobile.text.toString().trim()
           if(mobile.isEmpty()||mobile.length<10)
           {
               Utils.showMessage("Please enter valid mobile number",applicationContext)
               return@setOnClickListener
           }
            callOTP()

        }

    }
    fun callOTP() {
        val dialog= CustomDialog(applicationContext)
        // Obtain the DataManager instance
        dialog.showDialog(this@RegisterActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: LoginResponse? = response.body()

                    // Handle the response

                    model?.message?.let { Utils.showMessage(it,applicationContext) }

                    if(model?.status == true)
                    {
                        model.otp?.let {
                            val intent = Intent(this@RegisterActivity, OTPActivity::class.java)
                            intent.putExtra("mobileNumber",mobile)
                            intent.putExtra("otp",it)
                            startActivity(intent)
                            finish()
                           /* startActivity(Intent(this@RegisterActivity, OTPActivity::class.java))
                            overridePendingTransition(0, 0)*/
                        }
                    }
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.login(otpCallback,mobile)
    }
}