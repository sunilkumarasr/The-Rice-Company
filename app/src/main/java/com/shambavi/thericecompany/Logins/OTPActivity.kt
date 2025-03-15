package com.shambavi.thericecompany.Logins

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.LoginResponse
import com.gadiwalaUser.Models.OTPResponse
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Activitys.DashBoardActivity
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityOtpactivityBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OTPActivity : AppCompatActivity() {

    val binding: ActivityOtpactivityBinding by lazy {
        ActivityOtpactivityBinding.inflate(layoutInflater)
    }
var mobileNumber=""
var otp=""
    fun AppCompatEditText.showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        this.requestFocus()
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        type = intent.getStringExtra("type").toString()
        mobileNumber=intent.getStringExtra("mobileNumber").toString()
        inits()

    }

    private fun inits() {


        //OTP
        var count = 0
        fun setFocusable(){
            count++
            binding.pinEdit1.isFocusable = true
            binding.pinEdit1.isFocusableInTouchMode = true
            binding.pinEdit2.isFocusable = true
            binding.pinEdit2.isFocusableInTouchMode = true
            binding.pinEdit3.isFocusable = true
            binding.pinEdit3.isFocusableInTouchMode = true
            binding.pinEdit4.isFocusable = true
            binding.pinEdit4.isFocusableInTouchMode = true
        }
        binding.pinEdit1.setOnClickListener {
            if(count==0){
                setFocusable()
                binding.pinEdit1.requestFocus()
                binding.pinEdit1.showKeyboard()
            }
        }
        binding.pinEdit2.setOnClickListener {
            if(count==0){
                setFocusable()
                binding.pinEdit2.requestFocus()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.pinEdit2.showKeyboard()
                },100)

            }
        }
        binding.pinEdit3.setOnClickListener {
            if(count==0){
                setFocusable()
                binding.pinEdit3.requestFocus()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.pinEdit3.showKeyboard()
                },100)

            }
        }
        binding.pinEdit4.setOnClickListener {
            if(count==0){
                setFocusable()
                binding.pinEdit4.requestFocus()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.pinEdit4.showKeyboard()
                },100)

            }
        }
        binding.pinEdit1.addTextChangedListener {
            if (it?.toString()?.length == 1) binding.pinEdit2.requestFocus()
        }
        binding.pinEdit2.addTextChangedListener {
            if (it?.toString()?.length == 1) binding.pinEdit3.requestFocus() else binding.pinEdit1.requestFocus()
        }
        binding.pinEdit3.addTextChangedListener {
            if (it?.toString()?.length == 1) binding.pinEdit4.requestFocus() else binding.pinEdit2.requestFocus()
        }
        binding.pinEdit4.addTextChangedListener {
            if ((it?.toString()?.length?:0)<1) binding.pinEdit3.requestFocus()
        }


        binding.linearVerify.setOnClickListener {
            val pin1 = binding.pinEdit1.editableText.trim().toString()
            val pin2 = binding.pinEdit2.editableText.trim().toString()
            val pin3 = binding.pinEdit3.editableText.trim().toString()
            val pin4 = binding.pinEdit4.editableText.trim().toString()

            var otp: String = "$pin1$pin2$pin3$pin4"
            if(otp.isEmpty()||otp.length<4)
            {
                Utils.showMessage("Please enter OTP",applicationContext)
                return@setOnClickListener
            }
            submitOPT(otp)



        }

    }
    fun submitOPT(otp:String) {
        val dialog= CustomDialog(applicationContext)
        // Obtain the DataManager instance
        dialog.showDialog(this@OTPActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<OTPResponse> {
            override fun onResponse(call: Call<OTPResponse>, response: Response<OTPResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: OTPResponse? = response.body()

                    // Handle the response

                    model?.message?.let { Utils.showMessage(it,applicationContext) }

                    if(model?.status == true)
                    {
                        val user=model.data
                        user!!?.let {
                            MyPref.setUser(applicationContext,
                                it.usersId!!,it.phone!!,it.fullName!!,it.email!!)
                        }
                        if(model.data!!.profile_status==1){

                            startActivity(Intent(this@OTPActivity, DashBoardActivity::class.java))
                        }else{
                            val intent = Intent(this@OTPActivity, AddAddressActivity::class.java)
                            startActivity(intent)


                        }
                        finish()

                    }
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")
                }
            }

            override fun onFailure(call: Call<OTPResponse>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.verifyOtp(otpCallback,mobileNumber,otp)
    }
}