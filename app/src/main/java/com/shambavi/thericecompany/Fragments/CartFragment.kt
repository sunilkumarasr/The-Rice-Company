package com.shambavi.thericecompany.Fragments

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.CartMainRes
import com.gadiwalaUser.Models.CouponsMainRes
import com.gadiwalaUser.Models.MainResponse
import com.gadiwalaUser.Models.PincodeMainRes
import com.gadiwalaUser.Models.Pincodes
import com.gadiwalaUser.services.DataManager
import com.royalit.motherchoice.utils.NetWorkConection
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Activitys.DashBoardActivity
import com.shambavi.thericecompany.Activitys.SlotsActivity
import com.shambavi.thericecompany.cart.AddressListActivity
import com.shambavi.thericecompany.cart.CartAdapter
import com.shambavi.thericecompany.cart.OrderSuccessActivity
import com.shambavi.thericecompany.databinding.FragmentCartBinding
import com.shambavi.thericecompany.listeners.ProductListener
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CartFragment : Fragment() ,ProductListener{
    lateinit var cartAdapter: CartAdapter
    var user_id=""
    var addres_id=""
    var pincode=""
    var addres=""
    var slot_id=""
    var slot_time=""
    var payment_id="12"
    var product_ids=""
    var cart_ids=""
    var qnts=""
    var couponcode=""
    var couponAmount=""
    var delivery_charges=0
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
        if(!NetWorkConection.isNEtworkConnected(requireActivity()))
        {
            AlertDialog.Builder(requireActivity())
                .setTitle("Alert!")
                .setMessage("No Network available")
                .show()
        }

    }

    private fun init() {
        user_id= MyPref.getUser(requireActivity())
        addres_id= MyPref.getAddressId(requireActivity())
        addres= MyPref.getAddress(requireActivity())
        pincode= MyPref.getPincodeId(requireActivity())
        binding.tvBillAmount.paintFlags = binding.tvBillAmount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.tvDeliveryChargesStrike.paintFlags = binding.tvBillAmount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        if(addres.isEmpty())
            binding.tvAddress.text="Please select Delivery Address"
        else {
            binding.tvAddress.text = addres
            var type = MyPref.getAddressType(requireActivity())
            if (type.isEmpty())
                binding.txtAdrsType.visibility = View.INVISIBLE
            else
                binding.txtAdrsType.visibility = View.VISIBLE
            binding.txtAdrsType.text = type
        }

        cartAdapter=CartAdapter()
        cartAdapter.setListener(this)
        binding.recyclerCart.layoutManager=
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
        binding.recyclerCart.adapter=cartAdapter
        if(!NetWorkConection.isNEtworkConnected(requireActivity()))
        {
            AlertDialog.Builder(requireActivity())
                .setTitle("Alert!")
                .setMessage("No Network available")
                .show()
        }

        getCart()
        getPincodeList()
        binding.btnBack.visibility=View.GONE
       /* binding.btnBack.setOnClickListener {
            finish()
        }*/
        binding.btnChangeSlot.setOnClickListener {
            if(!NetWorkConection.isNEtworkConnected(requireActivity()))
            {
                AlertDialog.Builder(requireActivity())
                    .setTitle("Alert!")
                    .setMessage("No Network available")
                    .show()
                return@setOnClickListener
            }
            slotlistener.launch(Intent(requireContext(),SlotsActivity::class.java))

            // startActivityForResult(Intent(requireActivity(), SlotsActivity::class.java),1)
        }



        binding.btnPayment.setOnClickListener {
            if(addres_id.isEmpty())
            {
                Utils.showMessage("Select Delivery Address",requireActivity())
                return@setOnClickListener
            }
            if(!isPincodeAvaiable())
            {
                Utils.showMessage("This service not available in this pincode",requireActivity())
                return@setOnClickListener
            }


            if(slot_id.isEmpty())
            {
                Utils.showMessage("Select Delivery slot",requireActivity())
                return@setOnClickListener
            }
            if(product_ids.isEmpty())
            {
                Utils.showMessage("Cart is Empty",requireActivity())
                return@setOnClickListener
            }
            if(!NetWorkConection.isNEtworkConnected(requireActivity()))
            {
                AlertDialog.Builder(requireActivity())
                    .setTitle("Alert!")
                    .setMessage("No Network available")
                    .show()
                return@setOnClickListener
            }
            placeOrder()
        }
        binding.btnChangeAddress.setOnClickListener {

            addressLauncher.launch(Intent(requireActivity(), AddressListActivity::class.java))
        }

        binding.applyCouponButton.setOnClickListener {
            couponcode=binding.couponEditText.text.toString().trim()
            if(couponcode.isEmpty())
            {
                Utils.showMessage("Enter Coupon code",requireContext())
                return@setOnClickListener
            }
          applyCoupon()
        }
        binding.lnrRemove.setOnClickListener {
            couponAmount=""
            couponcode=""
            binding.couponEditText.setText("")
            calculateAmount()
            binding.tableCoupon.visibility=View.VISIBLE
            binding.tableApplied.visibility=View.GONE
        }
    }

    private fun isPincodeAvaiable(): Boolean {

        pincodeList.forEach {
            Log.e("Pincode","Pincode  $pincode - ${it.pincode} ${it.pincode.equals(pincode)}")
            if(it.pincode.equals(pincode)){
                return true
            }
        }
return false
    }
    private val slotlistener = registerForActivityResult( ActivityResultContracts.StartActivityForResult() ) {
            result -> if (result.resultCode == RESULT_OK) {
        var data=result.data
        slot_id=data!!.getStringExtra("slot_id").toString()
        slot_time=data!!.getStringExtra("slot_time").toString()

        binding.tvDeliverySlot.text="$slot_time"

    } }
    private val addressLauncher = registerForActivityResult( ActivityResultContracts.StartActivityForResult() ) {
            result -> if (result.resultCode == RESULT_OK) {
        addres_id=MyPref.getAddressId(requireActivity())
        addres=MyPref.getAddress(requireActivity())
        pincode= MyPref.getPincodeId(requireActivity())
        binding.tvAddress.text=addres
        var type=MyPref.getAddressType(requireActivity())
        if(type.isEmpty())
            binding.txtAdrsType.visibility=View.INVISIBLE
        else
            binding.txtAdrsType.visibility=View.VISIBLE
        binding.txtAdrsType.text=type
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

                   if( model.delivery_charges!=null&& model.delivery_charges!!.isNotEmpty())
                   {
                       delivery_charges=Integer.parseInt(model.delivery_charges)
                   }
                    product_ids=""
                    cartAdapter.cartList.forEach {
                        product_ids=product_ids+it.productId+","
                        cart_ids=cart_ids+it.cartId+","
                        qnts=qnts+it.quantity+","
                    }
                    calculateAmount()
                    checkData()
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
    var pincodeList=ArrayList<Pincodes>()
    fun getPincodeList()
    {

      //  val dialog= CustomDialog(requireActivity())
        // Obtain the DataManager instance
       // dialog.showDialog(requireActivity(),false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<PincodeMainRes> {
            override fun onResponse(call: Call<PincodeMainRes>, response: Response<PincodeMainRes>) {
         //       dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: PincodeMainRes? = response.body()

                    pincodeList.clear()
                    pincodeList=model!!.data
                    // Handle the response

                    // model?.message?.let { Utils.showMessage(it,requireActivity()) }


                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<PincodeMainRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
            //    dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.getPincodeList(otpCallback, user_id  )
    }
    fun applyCoupon()
    {

        //  val dialog= CustomDialog(requireActivity())
        // Obtain the DataManager instance
        // dialog.showDialog(requireActivity(),false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<CouponsMainRes> {
            override fun onResponse(call: Call<CouponsMainRes>, response: Response<CouponsMainRes>) {
                //       dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: CouponsMainRes? = response.body()

                    if(model!!.status == true) {
                        binding.tableCoupon.visibility = View.GONE
                        binding.tableApplied.visibility = View.VISIBLE
                      //  binding.applyCouponButton
                        couponAmount=(model!!.data!!.discount!!)
                        binding.txtAmountCoupon.setText("Coupon Applied (${Utils.RUPEE_SYMBOL}${couponAmount})")

                    }else
                    {
                        binding.tableCoupon.visibility=View.VISIBLE
                        binding.tableApplied.visibility=View.GONE
                        binding.txtAmountCoupon.setText("Coupon Applied")

                        couponAmount=""
                        Utils.showMessage(model!!.message!!,requireContext())
                    }
                    calculateAmount()
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<CouponsMainRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                //    dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.getCouponDetails(otpCallback, user_id,couponcode,totalAmount.toString()  )
    }
    fun deleteCart(cart_id: String)
    {
        updateCoupondata()
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
                    (requireActivity() as DashBoardActivity) .getCartCount()



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
        updateCoupondata()
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

    override fun onProductClick(product_id: String) {
        TODO("Not yet implemented")
    }


    var totalAmount=0
    var mrpAmount=0
    var discountedAmount=0
    var gst_charges=0
    fun updateCoupondata()
    {
        binding.lnrRemove.performClick()
    }
    fun calculateAmount()
    {
        mrpAmount=0
        totalAmount=0
        discountedAmount=0
        gst_charges=0
        cartAdapter.cartList.forEach {
            mrpAmount=mrpAmount+(Integer.parseInt(it.mrpPrice)*Integer.parseInt(it.quantity))
            discountedAmount=discountedAmount+(Integer.parseInt(it.ourPrice)*Integer.parseInt(it.quantity))
            totalAmount=totalAmount+(Integer.parseInt(it.ourPrice)*Integer.parseInt(it.quantity))
            if(it.gst!!.isNotEmpty())
                gst_charges=gst_charges+((Integer.parseInt(it.gst)*(Integer.parseInt(it.quantity)*Integer.parseInt(it.ourPrice)))/100)
        }

        if(couponAmount.isNotEmpty()) {
            totalAmount = totalAmount + delivery_charges + gst_charges-Integer.parseInt(couponAmount)
            binding.txtSaved.setText("Saved ${Utils.RUPEE_SYMBOL}${mrpAmount+Integer.parseInt(couponAmount)-(discountedAmount)}")

        }
                else{
            totalAmount = totalAmount + delivery_charges + gst_charges
            binding.txtSaved.setText("Saved ${Utils.RUPEE_SYMBOL}${mrpAmount-(discountedAmount)}")

        }
        binding.tvDiscountedAmount.text="${Utils.RUPEE_SYMBOL}$discountedAmount"
        binding.tvBillAmount.text="${Utils.RUPEE_SYMBOL}$mrpAmount"
        binding.tvGrandTotal.text="${Utils.RUPEE_SYMBOL}$totalAmount"
        binding.tvDeliveryCharges.text="${Utils.RUPEE_SYMBOL}$delivery_charges"
        binding.tvGstCharges.text="${Utils.RUPEE_SYMBOL}$gst_charges"



    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==200)
        {
            slot_id=data!!.getStringExtra("slot_id").toString()
            slot_time=data!!.getStringExtra("slot_time").toString()

            binding.tvDeliverySlot.text="$slot_time"
        }
    }*/
    private fun checkData() {

        if(cartAdapter.itemCount>0)
        {
            binding.txtNoData.visibility= View.GONE
            binding.recyclerCart.visibility= View.VISIBLE
            binding.scrollview.visibility= View.VISIBLE

            binding.lnrCart.visibility=View.VISIBLE
            binding.lnrBottom.visibility=View.VISIBLE
            binding.txtCount.setText("${cartAdapter.itemCount} Item(s)")
        }else
        {
            binding.txtNoData.visibility= View.VISIBLE
            binding.recyclerCart.visibility= View.GONE
            binding.lnrCart.visibility=View.GONE
            binding.scrollview.visibility=View.GONE
            binding.lnrBottom.visibility=View.GONE

        }
    }
    fun placeOrder()
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

                    // model?.Message?.let { Utils.showMessage(it,applicationContext) }

                    if(model!!.Status !!)
                    {
                        if(couponAmount.isEmpty())
                            couponAmount="0";
                        var saved=  mrpAmount+Integer.parseInt(couponAmount)-discountedAmount
                        val intent= Intent(requireActivity(),OrderSuccessActivity::class.java)
                        intent.putExtra("saved",saved.toString())
                        intent.putExtra("OrderID",model.order_id)
                        startActivity(intent)
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
        dataManager.placeOrder(otpCallback, user_id  ,payment_id,addres_id,totalAmount.toString(), product_ids,qnts,slot_id,cart_ids,gst_charges.toString(),couponcode, couponAmount )
    }
}