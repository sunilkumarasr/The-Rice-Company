package com.shambavi.thericecompany.products

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityCategoryProductsBinding

class CategoryProductsActivity : AppCompatActivity() {
    lateinit var binding:ActivityCategoryProductsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCategoryProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            finish()
        }
    }
}