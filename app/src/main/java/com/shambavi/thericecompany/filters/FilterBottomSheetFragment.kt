package com.shambavi.thericecompany.filters

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shambavi.thericecompany.databinding.FragmentFilterBinding

class FilterBottomSheetFragment: BottomSheetDialogFragment() {
    private lateinit var binding: FragmentFilterBinding
    private val filterAdapter = FilterAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadFilterData()
        setupClickListeners()
        binding.imgClose.setOnClickListener {
            dismiss()
        }
    }

    private fun loadFilterData() {
        val filterSections = listOf(
            FilterSection("Price", listOf(
                "Below ₹99",
                "₹1000 - ₹1500",
                "₹1500 - ₹3000",
                "Above 3000"
            )),
            FilterSection("Brand", listOf(
                "India Gate",
                "Fortune",
                "Daawat",
                "Royal"
            )),
            FilterSection("Type", listOf(
                "Basmati",
                "Brown",
                "White",
                "Jasmine"
            )),
            FilterSection("Kgs", listOf(
                "1 Kg",
                "5 Kg",
                "10 Kg",
                "25 Kg"
            ))
        )
        filterAdapter.submitList(filterSections)
    }

    private fun setupRecyclerView() {
        binding.rvFilters.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = filterAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnApply.setOnClickListener {
            val selectedFilters = filterAdapter.getSelectedFilters()
            (activity as? FilterCallback)?.onFiltersApplied(selectedFilters)
            dismiss()
        }

        binding.tvClearAll.setOnClickListener {
           // filterAdapter.clearFilters()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            bottomSheet?.setBackgroundResource(android.R.color.transparent)
        }
        return dialog
    }

    interface FilterCallback {
        fun onFiltersApplied(filters: Map<String, Set<String>>)
    }

    companion object {
        const val TAG = "FilterBottomSheet"
        fun newInstance() = FilterBottomSheetFragment()
    }
}
