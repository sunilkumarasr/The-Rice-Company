package com.shambavi.thericecompany.Activitys

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.NotificationMain
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityNotificationsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsActivity : AppCompatActivity() {

    val binding: ActivityNotificationsBinding by lazy {
        ActivityNotificationsBinding.inflate(layoutInflater)
    }
    lateinit var notificationAdapter: NotificationAdapter
    var user_id=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)
        user_id= MyPref.getUser(applicationContext)

        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Notifications"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }
        notificationAdapter=NotificationAdapter()
        binding.listNotification.adapter=notificationAdapter
        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{

        }
        getNotifications()
    }

    fun getNotifications()
    {

        val dialog= CustomDialog(this@NotificationsActivity)
        // Obtain the DataManager instance
        // dialog.showDialog(activity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<NotificationMain> {
            override fun onResponse(call: Call<NotificationMain>, response: Response<NotificationMain>) {
                // dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: NotificationMain? = response.body()

                    // Handle the response

                    ////model?.message?.let { Utils.showMessage(it,requireActivity()) }

                    if(model?.status == true)
                    {

                        notificationAdapter.setLists(model.data)

                    }else
                    {

                    }
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<NotificationMain>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                // dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.getNotifications(otpCallback,user_id)
    }
}