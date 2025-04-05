package com.shambavi.thericecompany.orders

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.gadiwalaUser.Models.OrderMainResponse
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityOrderDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailsActivity : AppCompatActivity() {
    private var orderId: String? = null
    private var currentRating: Float = 0f
    private lateinit var binding: ActivityOrderDetailsBinding

    companion object {
        const val EXTRA_ORDER_ID = "order_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        orderId = intent.getStringExtra(EXTRA_ORDER_ID)
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

    private fun displayOrderDetails(orderDetails: OrderDetails) {
        with(binding) {
            // Order ID
            orderIdText.text = "Order ID: ${orderDetails.orderId}"

            // Product Details
            Glide.with(this@OrderDetailsActivity)
                .load(orderDetails.productImage)
                .into(productImage)

            productWeightText.text = orderDetails.productWeight
            productNameText.text = orderDetails.productName
            productDescriptionText.text = orderDetails.productDescription
            priceText.text = "₹${orderDetails.price}"

            // Order Status Timeline
            displayOrderTimeline(orderDetails.orderStatus)

            // Shipping Details
            customerNameText.text = orderDetails.shippingDetails.customerName
            addressText.text = orderDetails.shippingDetails.address
            phoneText.text = "Phone Number: ${orderDetails.shippingDetails.phoneNumbers.joinToString(", ")}"

            // Rating
            if (orderDetails.rating != null) {
                ratingBar.rating = orderDetails.rating!!
            }

            // Billing Details
            displayBillingDetails(orderDetails.billingDetails)
        }
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
    fun getOrderDetails()
    {

        val dialog= CustomDialog(this@OrderDetailsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@OrderDetailsActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<OrderMainResponse> {
            override fun onResponse(call: Call<OrderMainResponse>, response: Response<OrderMainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: OrderMainResponse? = response.body()


                    println("OTP Sent successfully: ${model?.message}")
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
        orderId?.let { dataManager.getOrderDetails(otpCallback, it) }
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
}

