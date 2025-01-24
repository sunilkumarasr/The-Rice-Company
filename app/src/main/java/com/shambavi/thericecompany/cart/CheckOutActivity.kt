package com.shambavi.thericecompany.cart

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityCheckOutBinding
import com.shambavi.thericecompany.databinding.ActivitySplashBinding

class CheckOutActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnChangeSlot.setOnClickListener {

        }


        binding.btnPlus.setOnClickListener {

        }
        binding.btnMinus.setOnClickListener {

        }
        binding.btnPayment.setOnClickListener {

        }
        binding.btnChangeAddress.setOnClickListener {

            addressLauncher.launch(Intent(applicationContext,AddressListActivity::class.java))
        }

    }

    private val addressLauncher = registerForActivityResult( ActivityResultContracts.StartActivityForResult() ) {
            result -> if (result.resultCode == RESULT_OK) {
        val filters = result.data?.getSerializableExtra("filters") as? String // Apply filters to your data
    } }
}