package com.shambavi.thericecompany.Fragments

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.CartMainRes
import com.gadiwalaUser.Models.MainResponse
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Activitys.SlotsActivity
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.cart.AddressListActivity
import com.shambavi.thericecompany.cart.CartAdapter
import com.shambavi.thericecompany.databinding.ActivityCheckOutBinding
import com.shambavi.thericecompany.databinding.FragmentCartBinding
import com.shambavi.thericecompany.databinding.FragmentHomeBinding
import com.shambavi.thericecompany.listeners.ProductListener
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CartFragment : Fragment() ,ProductListener{
    lateinit var cartAdapter: CartAdapter
    var user_id=""
    var addres_id=""
    var addres=""
    var slot_id=""
    var slot_time=""

    private lateinit var binding: FragmentCartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {
        user_id= MyPref.getUser(requireActivity())
        addres_id= MyPref.getAddressId(requireActivity())
        addres= MyPref.getAddress(requireActivity())
        if(addres.isEmpty())
            binding.tvAddress.text="Please select Delivery Address"
        else
            binding.tvAddress.text=addres
        cartAdapter=CartAdapter()
        cartAdapter.setListener(this)
        binding.recyclerCart.layoutManager=
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerCart.adapter=cartAdapter
        getCart()
        binding.btnBack.visibility=View.GONE
       /* binding.btnBack.setOnClickListener {
            finish()
        }*/
        binding.btnChangeSlot.setOnClickListener {

            startActivityForResult(Intent(requireActivity(), SlotsActivity::class.java),1)
        }



        binding.btnPayment.setOnClickListener {
            if(addres_id.isEmpty())
            {
                Utils.showMessage("Select Delivery Address",requireActivity())
                return@setOnClickListener
            }
        }
        binding.btnChangeAddress.setOnClickListener {

            addressLauncher.launch(Intent(requireActivity(), AddressListActivity::class.java))
        }

    }

    private val addressLauncher = registerForActivityResult( ActivityResultContracts.StartActivityForResult() ) {
            result -> if (result.resultCode == RESULT_OK) {
        addres_id= MyPref.getAddressId(requireActivity())
        addres= MyPref.getAddress(requireActivity())
        binding.tvAddress.text=addres
    } }

    fun getCart()
    {

        val dialog= CustomDialog(requireActivity())
        // Obtain the DataManager instance
        dialog.showDialog(requireActivity(),false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<CartMainRes> {
            override fun onResponse(call: Call<CartMainRes>, response: Response<CartMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: CartMainRes? = response.body()

                    // Handle the response

                    // model?.message?.let { Utils.showMessage(it,requireActivity()) }

                    cartAdapter.cartList.clear()
                    cartAdapter.cartList.addAll(model!!.data)
                    cartAdapter.notifyDataSetChanged()
                    calculateAmount()
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<CartMainRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.getCart(otpCallback, user_id  )
    }
    fun deleteCart(cart_id: String)
    {

        val dialog= CustomDialog(requireActivity())
        // Obtain the DataManager instance
        dialog.showDialog(requireActivity(),false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                    //model?.Message?.let { Utils.showMessage(it,requireActivity()) }

                    getCart()


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
        dataManager.deleteProduct(otpCallback, user_id,cart_id  )
    }

    fun updateCart(cart_id:String,quantity:String)
    {

        val dialog= CustomDialog(requireActivity())
        // Obtain the DataManager instance
        dialog.showDialog(requireActivity(),false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                    // model?.Message?.let { Utils.showMessage(it,requireActivity()) }

                    if(model!!.Status !!)
                    {
                        getCart()
                    }

                    println("OTP Sent successfully: ${model?.Message}")
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
        dataManager.updateCart(otpCallback, user_id  , cart_id ,quantity.toString() )
    }
    override fun addProduct(product_id: String, attribution_id: String) {

    }

    override fun deleteProduct(cart_id: String) {
        deleteCart(cart_id)
    }

    override fun updateProduct(cart_id: String, qnty: Int) {
        updateCart(cart_id,qnty.toString())
    }

    fun calculateAmount()
    {
        var totalAmount=0
        var discountedAmount=0
        cartAdapter.cartList.forEach {
            totalAmount=totalAmount+Integer.parseInt(it.ourPrice)
            discountedAmount=discountedAmount+Integer.parseInt(it.mrpPrice)
        }

        binding.tvDiscountedAmount.text="${Utils.RUPEE_SYMBOL} $totalAmount"
        binding.tvBillAmount.text="${Utils.RUPEE_SYMBOL} $discountedAmount"
        binding.tvGrandTotal.text="${Utils.RUPEE_SYMBOL} $discountedAmount"

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==200)
        {
            slot_id=data!!.getStringExtra("slot_id").toString()
            slot_time=data!!.getStringExtra("slot_time").toString()

            binding.tvDeliverySlot.text="$slot_time"
        }
    }

}