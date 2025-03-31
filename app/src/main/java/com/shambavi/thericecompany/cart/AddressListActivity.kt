package com.shambavi.thericecompany.cart

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.AddressDataMainRes
import com.gadiwalaUser.Models.CartMainRes
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Logins.AddAddressActivity
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityAddressListBinding
import com.shambavi.thericecompany.listeners.ProductListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressListActivity : AppCompatActivity(),ProductListener {
    private lateinit var binding: ActivityAddressListBinding
    private val addressAdapter = AddressAdapter()
var user_id=""
var addres_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user_id=MyPref.getUser(applicationContext)
        addres_id=MyPref.getAddressId(applicationContext)
        setupRecyclerView()
        setupClickListeners()
        getAddress()
        addressAdapter.setListners(this)
        binding.btnAddAddress.setOnClickListener {
            var intent=Intent(applicationContext,UpdateAddressActivity::class.java)

            startActivityForResult(intent,1)
        }
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)
        if(resultCode== RESULT_OK)
        {
            getAddress()
        }
    }

    private fun setupRecyclerView() {
        binding.rvAddresses.apply {
            layoutManager = LinearLayoutManager(this@AddressListActivity)
            adapter = addressAdapter
        }
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener { finish() }
        binding.btnAddAddress.setOnClickListener {
            startActivity(Intent(this, AddAddressActivity::class.java))
        }
    }

    fun getAddress()
    {

        val dialog= CustomDialog(this@AddressListActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@AddressListActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<AddressDataMainRes> {
            override fun onResponse(call: Call<AddressDataMainRes>, response: Response<AddressDataMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: AddressDataMainRes? = response.body()

                    // Handle the response

                    // model?.message?.let { Utils.showMessage(it,applicationContext) }

                    model!!.data.forEach {
                        if(it.id==addres_id)
                            it.isSelected=true
                    }
                    addressAdapter.currentList.clear()
                    addressAdapter.submitList(model!!.data)
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<AddressDataMainRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.getAddressDetails(otpCallback, user_id  )
    }

    override fun addProduct(product_id: String, attribution_id: String) {
        setResult(RESULT_OK)
        finish()
    }

    override fun deleteProduct(cart_id: String) {

    }

    override fun updateProduct(product_id: String, qnty: Int) {

    }


    /* private fun loadAddresses() {
         val addresses = listOf(
             AddressModel(
                 id = 1,
                 name = "Sai Kumar",
                 address = "9-1-34/305, Sri sai nilayam,\nBapunagar, Langer House, Hyderabad,\nTelangana-500008",
                 phone = "9123456789",
                 isSelected = true
             ),
             AddressModel(
                 id = 2,
                 name = "Ram Macha",
                 address = "9-1-34/300, Sri sai nilayam,\nBapunagar, Langer House, Hyderabad,\nTelangana-500008",
                 phone = "",
                 isSelected = false
             )
         )

     }*/
}

