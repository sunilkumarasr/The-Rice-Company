package com.shambavi.thericecompany.orders

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookiron.itpark.utils.MyPref
import com.bumptech.glide.Glide
import com.gadiwalaUser.Models.MainResponse
import com.gadiwalaUser.Models.OrderMainResponse
import com.gadiwalaUser.services.DataManager
import com.gadiwalaUser.services.DataManager.Companion.ROOT_URL
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityOrderDetailsBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailsActivity : AppCompatActivity() {
    private var orderId: String? = null
    private var ORDER_ID_NOT_NUMBER: String = ""
    private var currentRating: Float = 0f
    lateinit var orderProductAdapter: OrderProductAdapter
    private lateinit var binding: ActivityOrderDetailsBinding
    var user_id = ""

    companion object {
        const val EXTRA_ORDER_ID = "order_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(
            this,
            ContextCompat.getColor(this, R.color.colorPrimary),
            false
        )

        orderId = intent.getStringExtra(EXTRA_ORDER_ID)
        user_id = MyPref.getUser(applicationContext)
        orderProductAdapter = OrderProductAdapter()
        orderProductAdapter.setListener(this)
        binding.recyclerProducts.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerProducts.adapter = orderProductAdapter
        binding.addressText.text = ""
        setupUI()
        getOrderDetails()
        setupListeners()


    }

    private fun setupUI() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadOrderDetails() {
        // In a real app, you would fetch this from an API or database
        // val orderDetails = getSampleOrderDetails()
        // displayOrderDetails(orderDetails)
    }

    private fun displayOrderDetails(order: Order) {
        with(binding) {

            // Order ID
            orderIdText.text = "Order ID: ${order.orderId}"


            /* if(order.products.size>0) {
                 Glide.with(productImage)
                     .load(ROOT_URL + order.products.get(0).productImage)
                     .placeholder(R.drawable.item1)
                     .into(productImage)

                 productNameText.text=order.products.get(0).productTitle

                 productDescriptionText.text = order.products.get(0).productTitle
             }
             // Product Details

             productWeightText.text = order.qty

             priceText.text = "₹ ${order.amount}"*/

            // Order Status Timeline
            //displayOrderTimeline(orderDetails.orderStatus)
            if (order.status == "1") {
                lnrOrderRefund.visibility = View.GONE
                lnrOrderReturned.visibility = View.GONE
                /*lnrOrderDelivered.visibility= View.GONE
                lnrOrderConfirmed.visibility= View.VISIBLE
                lnrOrderShipped.visibility= View.GONE
                lnrOrderOut.visibility= View.GONE
*/


                /*viewOrderConfirmed.visibility=View.GONE
                viewOrderShipped.visibility=View.GONE
                viewOrderOutDelivery.visibility=View.GONE
                viewOrderDelivered.visibility=View.GONE
              */viewOrderDelivered.visibility = View.GONE
                viewOrderRetured.visibility = View.GONE

            } else if (order.status == "2") {

                lnrOrderRefund.visibility = View.GONE
                lnrOrderReturned.visibility = View.GONE
                /* lnrOrderDelivered.visibility= View.GONE
                 lnrOrderConfirmed.visibility= View.VISIBLE
                 lnrOrderShipped.visibility= View.VISIBLE
                 lnrOrderOut.visibility= View.GONE
 */
                /* viewOrderShipped.visibility=View.GONE
                 viewOrderOutDelivery.visibility=View.VISIBLE
                 viewOrderConfirmed.visibility=View.VISIBLE

                */
                viewOrderDelivered.visibility = View.GONE
                viewOrderRetured.visibility = View.GONE
                imgOrderShiiped.setImageResource(R.drawable.icon_order_check_success)
                viewOrderConfirmed.setBackgroundColor(resources.getColor(R.color.green))

            } else if (order.status == "3") {
                lnrOrderRefund.visibility = View.GONE
                lnrOrderReturned.visibility = View.GONE
                /* lnrOrderDelivered.visibility= View.GONE
                 lnrOrderConfirmed.visibility= View.VISIBLE
                 lnrOrderShipped.visibility= View.VISIBLE
                 lnrOrderOut.visibility= View.VISIBLE
 */
                /*   viewOrderShipped.visibility=View.VISIBLE
                   viewOrderOutDelivery.visibility=View.VISIBLE
                   viewOrderConfirmed.visibility=View.VISIBLE
                   viewOrderDelivered.visibility=View.GONE
                */
                viewOrderDelivered.visibility = View.GONE
                viewOrderRetured.visibility = View.GONE
                imgOrderShiiped.setImageResource(R.drawable.icon_order_check_success)
                imgOrderOut.setImageResource(R.drawable.icon_order_check_success)

                viewOrderConfirmed.setBackgroundColor(resources.getColor(R.color.green))
                viewOrderShipped.setBackgroundColor(resources.getColor(R.color.green))

            } else if (order.status == "4") {
                lnrOrderRefund.visibility = View.GONE
                lnrOrderReturned.visibility = View.GONE
                /* lnrOrderDelivered.visibility= View.VISIBLE
                 lnrOrderConfirmed.visibility= View.VISIBLE
                 lnrOrderShipped.visibility= View.VISIBLE
                 lnrOrderOut.visibility= View.VISIBLE
 */
                /*viewOrderShipped.visibility=View.VISIBLE
                viewOrderOutDelivery.visibility=View.VISIBLE
                viewOrderConfirmed.visibility=View.VISIBLE
                viewOrderDelivered.visibility=View.GONE
              */
                viewOrderDelivered.visibility = View.GONE
                viewOrderRetured.visibility = View.GONE
                viewOrderConfirmed.setBackgroundColor(resources.getColor(R.color.green))
                viewOrderShipped.setBackgroundColor(resources.getColor(R.color.green))
                viewOrderConfirmed.setBackgroundColor(resources.getColor(R.color.green))
                imgOrderShiiped.setImageResource(R.drawable.icon_order_check_success)
                imgOrderOut.setImageResource(R.drawable.icon_order_check_success)
                imgOrderDelivered.setImageResource(R.drawable.icon_order_check_success)

            }  else if (order.status == "5") {
                lnrOrderRefund.visibility = View.GONE
                lnrOrderReturned.visibility = View.VISIBLE
                /* lnrOrderDelivered.visibility= View.VISIBLE
                 lnrOrderConfirmed.visibility= View.VISIBLE
                 lnrOrderShipped.visibility= View.VISIBLE
                 lnrOrderOut.visibility= View.VISIBLE
 */
                /*viewOrderShipped.visibility=View.VISIBLE
                viewOrderOutDelivery.visibility=View.VISIBLE
                viewOrderConfirmed.visibility=View.VISIBLE
                viewOrderDelivered.visibility=View.GONE
              */
                viewOrderDelivered.visibility = View.VISIBLE
                viewOrderRetured.visibility = View.GONE

            } else if (order.status == "5") {
                lnrOrderRefund.visibility = View.VISIBLE
                lnrOrderReturned.visibility = View.VISIBLE
                /* lnrOrderDelivered.visibility= View.VISIBLE
                 lnrOrderConfirmed.visibility= View.VISIBLE
                 lnrOrderShipped.visibility= View.VISIBLE
                 lnrOrderOut.visibility= View.VISIBLE
 */
                /*viewOrderShipped.visibility=View.VISIBLE
                viewOrderOutDelivery.visibility=View.VISIBLE
                viewOrderConfirmed.visibility=View.VISIBLE
                viewOrderDelivered.visibility=View.GONE
              */
                viewOrderDelivered.visibility = View.VISIBLE
                viewOrderRetured.visibility = View.VISIBLE

            }else {

            }
            // Shipping Details
            customerNameText.text = order.fullName
            addressText.text = formAddress(order)
            phoneText.text = "Phone Number: ${order.mobile}"

            // Rating


            // Billing Details
            binding.txtTotalAmount.text = "₹${order.amount}"
            binding.txtGrandTotal.text = "₹${order.amount}"
            binding.txtGstCharges.text = "₹${order.gst_charges}"
            binding.txtDeliveryCharges.text = "₹${0}"


        }
    }

    fun formAddress(data: Order): String {
        var adrs =
            "${data.houseNo},${data.floor},${data.landmark}\n${data.cityTown},${data.state},${data.country},${data.zipCode}"
        adrs = adrs.replace(",,", ",")
        return adrs
    }

    private fun displayOrderTimeline(statusList: List<OrderStatus>) {/*
        statusList.forEachIndexed { index, status ->
            // In a real app, you would dynamically create these views
            // For demo purposes, we're using pre-defined views in XML
            val statusViews = listOf(
                binding.orderConfirmedLayout,
                binding.deliveredLayout,
                binding.returnLayout,
                binding.refundLayout
            )

            if (index < statusViews.size) {
                with(statusViews[index]) {
                    visibility = View.VISIBLE
                    findViewById<ImageView>(R.id.statusIcon).setColorFilter(
                        ContextCompat.getColor(this@OrderDetailsActivity, status.statusColor)
                    )
                    findViewById<TextView>(R.id.statusText).text = "${status.status}, ${status.date}"
                }
            }
        }*/
    }

    private fun displayBillingDetails(billing: BillingDetails) {
        /*with(binding) {
            // Bill Amount
            billAmountValue.text = "₹${billing.itemPrice}"
            savingsText.text = "Saved ₹${billing.savings}"

            // Delivery Charges
            deliveryChargeValue.text = "₹${billing.deliveryCharge}"

            // Free Delivery Message
            if (billing.remainingForFreeDelivery > 0) {
                freeDeliveryText.visibility = View.VISIBLE
                freeDeliveryText.text = "Shop for ₹${billing.remainingForFreeDelivery} more to get free delivery"
            } else {
                freeDeliveryText.visibility = View.GONE
            }

            // Grand Total
            grandTotalValue.text = "₹${billing.grandTotal}"
        }*/
    }

    private fun setupListeners() {
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                currentRating = rating
                submitRating()
            }
        }
    }

    private fun submitRating() {
        // In a real app, you would submit this to your backend
        Toast.makeText(
            this,
            "Thank you for rating the product!",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun getOrderDetails() {

        val dialog = CustomDialog(this@OrderDetailsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@OrderDetailsActivity, false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<OrderMainResponse> {
            override fun onResponse(
                call: Call<OrderMainResponse>,
                response: Response<OrderMainResponse>
            ) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: OrderMainResponse? = response.body()
                    ORDER_ID_NOT_NUMBER = model!!.orders.get(0).orderId.toString()
                    displayOrderDetails(model!!.orders.get(0))
                    println("OTP Sent successfully: ${model?.message}")
                    orderProductAdapter.cartList.clear()
                    orderProductAdapter.cartList.addAll(model!!.orders.get(0).products)
                    //  orderProductAdapter.cartList.addAll(model!!.orders.get(0).products)
                    orderProductAdapter.notifyDataSetChanged()
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<OrderMainResponse>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        orderId?.let { dataManager.getOrderDetails(otpCallback, it, user_id) }
    }
    /*
        private fun getSampleOrderDetails(): OrderDetails {
            return OrderDetails(
                orderId = "OD123456",
                productImage = "sample_image_url",
                productName = "Daawat Super Basmati Rice",
                productWeight = "Basmati (1 Kg)",
                productDescription = "(Fluffy Long Grains)",
                price = 62.0,
                orderStatus = listOf(
                    OrderStatus(
                        status = "Order Confirmed",
                        date = "25 Oct 2024",
                        isCompleted = true,
                        statusColor = R.color.green
                    ),
                    OrderStatus(
                        status = "Delivered",
                        date = "28 Oct 2024",
                        isCompleted = true,
                        statusColor = R.color.green
                    ),
                    OrderStatus(
                        status = "Return",
                        date = "29 Oct 2024",
                        isCompleted = true,
                        statusColor = R.color.gold
                    ),
                    OrderStatus(
                        status = "Refund",
                        date = "29 Oct 2024",
                        isCompleted = true,
                        statusColor = R.color.gold
                    )
                ),
                shippingDetails = ShippingDetails(
                    customerName = "Sai Kumar",
                    address = "Ho.No. 9-1-34/30/40D, Bapunagar,\nLonger House, Hyderabad,\nTelangana- 500008",
                    phoneNumbers = listOf("9123456789", "9876543210")
                ),
                billingDetails = BillingDetails(
                    itemPrice = 62.0,
                    savings = 5.0,
                    deliveryCharge = 30.0,
                    freeDeliveryThreshold = 131.0 // 62 + 69 more for free delivery
                ),
                rating = null
            )
        }
    */

    fun setRating(pid: String, rating: String) {
        val dialog = CustomDialog(this@OrderDetailsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@OrderDetailsActivity, false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                    model?.Message?.let { Utils.showMessage(it, applicationContext) }

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
        dataManager.setRating(otpCallback, user_id, pid, ORDER_ID_NOT_NUMBER, rating)
    }

}

