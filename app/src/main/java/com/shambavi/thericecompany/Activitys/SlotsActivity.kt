package com.shambavi.thericecompany.Activitys

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.gadiwalaUser.Models.CartMainRes
import com.gadiwalaUser.Models.SlotsMainRes
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.cart.SlotsAdapter
import com.shambavi.thericecompany.databinding.ActivityCheckOutBinding
import com.shambavi.thericecompany.databinding.ActivitySlotsBinding
import com.shambavi.thericecompany.listeners.ProductListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SlotsActivity : AppCompatActivity(), ProductListener {
    lateinit var binding: ActivitySlotsBinding
    lateinit var adapter: SlotsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySlotsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        adapter=SlotsAdapter()
        binding.rvSlots.layoutManager=LinearLayoutManager(applicationContext)
        binding.rvSlots.adapter=adapter
adapter.setListener(this)
        binding.ivBack.setOnClickListener {
            finish()
        }
        getSlots()

    }
    fun getSlots()
    {

        val dialog= CustomDialog(this@SlotsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@SlotsActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<SlotsMainRes> {
            override fun onResponse(call: Call<SlotsMainRes>, response: Response<SlotsMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: SlotsMainRes? = response.body()
                    adapter.setSlotLists(model!!.data)
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<SlotsMainRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.getSlots(otpCallback  )
    }
    //Used for Slot Selection in this class
    override fun addProduct(slot_id: String, slot_time: String) {
        val intent=Intent()
        intent.putExtra("slot_id",slot_id)
        intent.putExtra("slot_time",slot_time)
        setResult(RESULT_OK,intent)

        finish()
    }

    override fun deleteProduct(cart_id: String) {

    }


    override fun updateProduct(slot_id: String, slot_time: Int) {

    }

    override fun onProductClick(product_id: String) {
        TODO("Not yet implemented")
    }
}