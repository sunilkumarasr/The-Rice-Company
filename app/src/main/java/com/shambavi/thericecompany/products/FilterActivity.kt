package com.shambavi.thericecompany.products

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityFilterBinding
import com.shambavi.thericecompany.filters.FilterAdapter
import com.shambavi.thericecompany.filters.FilterSection

class FilterActivity : AppCompatActivity() {private lateinit var binding: ActivityFilterBinding
    private val filterAdapter = FilterAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        setupRecyclerView()
        loadFilterData()

        binding.btnApply.setOnClickListener {
            val selectedFilters = filterAdapter.getSelectedFilters()
            val intent=Intent()
            /*intent.putExtra("filters", selectedFilters)
                intent.putExtra("filters", selectedFilters)*/
            setResult(RESULT_OK, )
            finish()
        }
    }

    private fun setupRecyclerView() {
        binding.rvFilters.apply {
            layoutManager = LinearLayoutManager(this@FilterActivity)
            adapter = filterAdapter
        }
    }

    private fun loadFilterData() {
       // filterAdapter.submitList(filterSections)
    }
}
