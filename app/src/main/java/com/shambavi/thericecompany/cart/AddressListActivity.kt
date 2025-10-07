package com.shambavi.thericecompany.cart

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.AddressDataMainRes
import com.gadiwalaUser.Models.MainResponse
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.Logins.AddAddressActivity
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityAddressListBinding
import com.shambavi.thericecompany.listeners.ProductListener
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressListActivity : AppCompatActivity(),ProductListener {
    private lateinit var binding: ActivityAddressListBinding
    private val addressAdapter = AddressAdapter(this)
var user_id=""
var addres_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
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


    /*override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)

            getAddress()
        //}
    }*/

    private fun setupRecyclerView() {
        binding.rvAddresses.apply {
            layoutManager = LinearLayoutManager(this@AddressListActivity)
            adapter = addressAdapter
        }
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener { finish() }
        binding.btnAddAddress.setOnClickListener {
            startActivityForResult(Intent(this, AddAddressActivity::class.java),200)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getAddress()
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
                   // addressAdapter.currentList.clear()
                    addressAdapter.submitList(model!!.data)
                    checkData()
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

    override fun deleteProduct(address_id: String) {
        deleteAddress(address_id)
    }

    override fun updateProduct(product_id: String, qnty: Int) {

    }

    override fun onProductClick(product_id: String) {
        TODO("Not yet implemented")
    }

    fun deleteAddress(address_id: String)
    {

        val dialog= CustomDialog(this@AddressListActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@AddressListActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                     model?.Message?.let { Utils.showMessage(it,applicationContext) }

                    if(address_id==addres_id)
                    {
                        MyPref.clearAddress(applicationContext)
                    }
                    getAddress()



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
        dataManager.deleteAddress(otpCallback, address_id  )
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

    private fun checkData() {

        if(addressAdapter.itemCount>0)
        {
            binding.txtNoData.visibility= View.GONE
            binding.rvAddresses.visibility= View.VISIBLE
        }else
        {
            binding.txtNoData.visibility= View.VISIBLE
            binding.rvAddresses.visibility= View.GONE
        }
    }
}

