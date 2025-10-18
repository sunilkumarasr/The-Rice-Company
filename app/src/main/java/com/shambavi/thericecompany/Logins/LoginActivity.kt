package com.shambavi.thericecompany.Logins

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.gadiwalaUser.Models.LoginResponse
import com.gadiwalaUser.services.DataManager
import com.royalit.motherchoice.utils.NetWorkConection
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityLoginBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class LoginActivity : AppCompatActivity() {
    var mobile:String=""

    val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.VANILLA_ICE_CREAM)
        {
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

            // 2. Handle Window Insets to prevent content overlap
            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

                // Apply insets as padding to the root view.
                // This will push all content within binding.root away from the system bars.
                view.setPadding(insets.left, insets.top, insets.right, insets.bottom)

                // If specific views still overlap or need different behavior (e.g., a Toolbar
                // intended to sit behind a transparent status bar), you'll need to apply
                // padding or margins more selectively to those specific views or their containers.
                // For instance, to only pad the top of your contentFrame and bottom of navigationView:
                // binding.contentFrame.setPadding(insets.left, insets.top, insets.right, binding.contentFrame.paddingBottom)
                // binding.navigationView.setPadding(binding.navigationView.paddingLeft, binding.navigationView.paddingTop, binding.navigationView.paddingRight, insets.bottom)


                WindowInsetsCompat.CONSUMED
            }

        }
        inits()
    }

    private fun inits() {
        binding.linearLogin.setOnClickListener {

            mobile=   binding.editMobile.text.toString().trim()
            if(mobile.isEmpty()||mobile.length<10)
            {
                Utils.showMessage("Please enter valid mobile number",applicationContext)
                return@setOnClickListener
            }
            if(!NetWorkConection.isNEtworkConnected(this@LoginActivity))
            {
                Utils.showMessage("Please check Network connection",applicationContext)
                return@setOnClickListener
            }
            LoginService()
        }

        binding.linearRegister.setOnClickListener {
           startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
    }

    fun LoginService() {
        val dialog=CustomDialog(applicationContext)
        // Obtain the DataManager instance
        dialog.showDialog(this@LoginActivity,false)
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
                                    ChangeView(it,model.newUser)
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
    fun ChangeView(otp: String, newUser: Boolean){
        val intent = Intent(this, OTPActivity::class.java)
        intent.putExtra("mobileNumber",mobile)
        intent.putExtra("otp",otp)
        intent.putExtra("newUser",newUser)
        intent.putExtra("type","Login")
        startActivity(intent)
        finish()
    }
}