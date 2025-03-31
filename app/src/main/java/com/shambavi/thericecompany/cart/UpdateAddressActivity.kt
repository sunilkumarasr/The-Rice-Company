package com.shambavi.thericecompany.cart

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.OTPResponse
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Activitys.DashBoardActivity
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityUpdateAddressBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateAddressActivity : AppCompatActivity() {

    val binding: ActivityUpdateAddressBinding by lazy {
        ActivityUpdateAddressBinding.inflate(layoutInflater)
    }
    var user_id=""
    var mobile=""
    var full_name=""
    var email=""
    var city=""
    var state=""
    var type=""
    var country=""
    var landmark=""
    var zipcode=""
    var flat=""
    var floor=""
    var area=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        user_id=MyPref.getUser(applicationContext).toString()
        mobile=MyPref.getMobile(applicationContext).toString()
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        type="Shop"
        binding.editEmail.setText("${MyPref.getEmail(applicationContext)}")
        binding.editFullName.setText("${MyPref.getName(applicationContext)}")
        inits()
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun inits() {

        binding.radioType.setOnCheckedChangeListener { group, checkedId ->

                if(checkedId==R.id.radio_home)
                {
                    type="Home"
                }
                else  if(checkedId==R.id.radio_shop)
                {
                    type="Shop"
                }else  if(checkedId==R.id.radio_other)
                {
                    type="Other"
                }


        }
        binding.linearSubmit.setOnClickListener {
            full_name=binding.editFullName.text.toString()
            email=binding.editEmail.text.toString()
            flat=binding.editFlatNo.text.toString()
            floor=binding.editFloor.text.toString()
            area=binding.editArea.text.toString()
            landmark=binding.editLandmark.text.toString()
            city=binding.editCity.text.toString()
            state=binding.editState.text.toString()
            country=binding.editCountry.text.toString()
            zipcode=binding.editZipcode.text.toString()

            if(full_name.isEmpty()||email.isEmpty()||flat.isEmpty()||area.isEmpty()||city.isEmpty()||state.isEmpty()||country.isEmpty()||zipcode.isEmpty()||type.isEmpty())
            {
                Utils.showMessage("Please fill all details",applicationContext)
                return@setOnClickListener
            }
            addAddress()
        }
    }
    fun addAddress() {
        val dialog= CustomDialog(applicationContext)
        // Obtain the DataManager instance
        dialog.showDialog(this@UpdateAddressActivity,false)
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


                        setResult(RESULT_OK)
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
        dataManager.addAddress(otpCallback,mobile,user_id,full_name,type,flat,floor,area+","+landmark,city,state,country,zipcode)
    }
}