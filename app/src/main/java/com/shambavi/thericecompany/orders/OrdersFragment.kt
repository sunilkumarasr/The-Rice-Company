package com.shambavi.thericecompany.orders

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.OrderMainResponse
import com.gadiwalaUser.services.DataManager
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.databinding.FragmentOrdersBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersFragment : Fragment() {


    private lateinit var binding: FragmentOrdersBinding
    private lateinit var orderaAdapter: OrdersAdapter
var user_id=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user_id=MyPref.getUser(requireContext())
        setupRecyclerView()
        setupSearch()
        getOrders()
        binding.filterButton.setOnClickListener {
            val bottomSheet = DeliverySlotBottomSheet.newInstance()
            bottomSheet.show(childFragmentManager, DeliverySlotBottomSheet.TAG)

        }

    }

    private fun setupRecyclerView() {
        orderaAdapter = OrdersAdapter(
             // Replace with your data source
            onOrderClick = { order ->
                // Handle order click
            },
            onRateClick = { order ->
                // Handle rating click
            }
        )

        binding.ordersRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity().applicationContext)
            adapter = orderaAdapter
        }
    }

    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Implement search functionality
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.filterButton.setOnClickListener {
            // Handle filter click
        }
    }

    fun getOrders()
    {

        val dialog= CustomDialog(requireActivity())
        // Obtain the DataManager instance
        dialog.showDialog(requireActivity(),false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<OrderMainResponse> {
            override fun onResponse(call: Call<OrderMainResponse>, response: Response<OrderMainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: OrderMainResponse? = response.body()
                    orderaAdapter.setList(model!!.orders)
                    // Handle the response

                    // model?.Message?.let { Utils.showMessage(it,applicationContext) }

                    checkData()

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
        dataManager.getOrders(otpCallback, user_id  )
    }
    private fun checkData() {

        if(orderaAdapter.itemCount>0)
        {
            binding.txtNoData.visibility= View.GONE
            binding.ordersRecyclerView.visibility= View.VISIBLE
        }else
        {
            binding.txtNoData.visibility= View.VISIBLE
            binding.ordersRecyclerView.visibility= View.GONE
        }
    }
}
