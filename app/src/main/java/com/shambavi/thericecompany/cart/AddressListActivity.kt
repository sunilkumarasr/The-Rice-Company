package com.shambavi.thericecompany.cart

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.shambavi.thericecompany.Logins.AddAddressActivity
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityAddressListBinding

class AddressListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressListBinding
    private val addressAdapter = AddressAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupClickListeners()
        loadAddresses()
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

    private fun loadAddresses() {
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
        addressAdapter.submitList(addresses)
    }
}

