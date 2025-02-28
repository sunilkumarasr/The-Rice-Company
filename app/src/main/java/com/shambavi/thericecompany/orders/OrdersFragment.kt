package com.shambavi.thericecompany.orders

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.shambavi.thericecompany.databinding.FragmentOrdersBinding

class OrdersFragment : Fragment() {


    private lateinit var binding: FragmentOrdersBinding
    private lateinit var orderaAdapter: OrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()

        binding.filterButton.setOnClickListener {
            val bottomSheet = DeliverySlotBottomSheet.newInstance()
            bottomSheet.show(childFragmentManager, DeliverySlotBottomSheet.TAG)

        }

    }

    private fun setupRecyclerView() {
        orderaAdapter = OrdersAdapter(
            orders = getSampleOrders(), // Replace with your data source
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

    private fun getSampleOrders(): List<Order> {
        return listOf(
            Order(
                id = "1",
                productImage = "url_to_image",
                productName = "Daawat Super Basmati Rice",
                orderStatus = OrderStatusEnum.DELIVERY_EXPECTED,
                deliveryDate = "tomorrow"
            ),
            Order(
                id = "2",
                productImage = "url_to_image",
                productName = "Daawat Super Basmati Rice",
                orderStatus = OrderStatusEnum.CANCELLED,
                deliveryDate = "Wed Nov 06"
            ),
            Order(
                id = "3",
                productImage = "url_to_image",
                productName = "Daawat Super Basmati Rice",
                orderStatus = OrderStatusEnum.DELIVERED,
                deliveryDate = "Oct 31",
                rating = 4.5f
            )
            // Add more sample orders as needed
        )
    }
}
