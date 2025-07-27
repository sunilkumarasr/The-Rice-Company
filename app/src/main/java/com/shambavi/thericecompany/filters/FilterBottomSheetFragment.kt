package com.shambavi.thericecompany.filters

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.FilterMainResp
import com.gadiwalaUser.Models.PriceRange
import com.gadiwalaUser.services.DataManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.databinding.FragmentFilterBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilterBottomSheetFragment: BottomSheetDialogFragment() {
    private lateinit var binding: FragmentFilterBinding
    private val filterAdapter = FilterAdapter()
    var user_id=""

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
        user_id= MyPref.getUser(requireActivity())

        setupRecyclerView()
        getFilters()

        setupClickListeners()
        binding.imgClose.setOnClickListener {
            dismiss()
        }
    }

    fun getFilters()
    {

        val dialog= CustomDialog(requireActivity())
        // Obtain the DataManager instance
       // dialog.showDialog(this@AllProductsActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<FilterMainResp> {
            override fun onResponse(call: Call<FilterMainResp>, response: Response<FilterMainResp>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: FilterMainResp? = response.body()

                    // Handle the response



                    if(model?.status == true)
                    {

                        loadFilterData(model!!.data!!)
                    }else
                    {
                        model?.message?.let { Utils.showMessage(it,requireActivity()) }
                    }

                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<FilterMainResp>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()

            }
        }

        // Call the sendOtp function in DataManager

            dataManager.getFilters(otpCallback,user_id)
    }
    private fun loadFilterData(data: ArrayList<PriceRange>) {
        val filterSections = listOf(
            FilterSection("Price",
                data),

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
            filterAdapter.clearFilters()
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
