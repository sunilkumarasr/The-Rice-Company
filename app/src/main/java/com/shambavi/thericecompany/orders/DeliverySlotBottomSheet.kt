package com.shambavi.thericecompany.orders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shambavi.thericecompany.R

class DeliverySlotBottomSheet : BottomSheetDialogFragment() {

    private lateinit var dateRecyclerView: RecyclerView
    private lateinit var timeSlotRecyclerView: RecyclerView
    private lateinit var applyButton: Button
    private lateinit var closeButton: ImageButton
    private lateinit var radioGrp: RadioGroup

    private var selectedDate: DeliveryDate? = null
    private var selectedTimeSlot: TimeSlot? = null

    private val dateAdapter by lazy {
        FilterDateAdapter(requireContext(), generateDates()) { date ->
            selectedDate = date
            // You might want to refresh available time slots based on selected date
            updateTimeSlots()
        }
    }

    private val timeSlotAdapter by lazy {
        TimeSlotAdapter(requireContext(), generateTimeSlots()) { timeSlot ->
            selectedTimeSlot = timeSlot
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_filter_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateRecyclerView = view.findViewById(R.id.date_recycler_view)
        timeSlotRecyclerView = view.findViewById(R.id.time_slot_recycler_view)
        applyButton = view.findViewById(R.id.apply_button)
        closeButton = view.findViewById(R.id.close_button)
        radioGrp = view.findViewById(R.id.radioGrp)

        setupRecyclerViews()
        setupListeners()
        radioGrp.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId==R.id.radio_order_time)
            {
                dateRecyclerView.visibility=View.VISIBLE
                timeSlotRecyclerView.visibility=View.GONE
            }else  if(checkedId==R.id.radio_order_status)
            {
                dateRecyclerView.visibility=View.GONE
                timeSlotRecyclerView.visibility=View.VISIBLE
            }
        }
        dateRecyclerView.visibility=View.GONE
        timeSlotRecyclerView.visibility=View.VISIBLE
    }

    private fun setupRecyclerViews() {
        // Setup date recycler view
        dateRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        dateRecyclerView.adapter = dateAdapter

        // Setup time slot recycler view
        timeSlotRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        timeSlotRecyclerView.adapter = timeSlotAdapter
    }

    private fun setupListeners() {
        closeButton.setOnClickListener {
            dismiss()
        }

        applyButton.setOnClickListener {
            if (selectedDate != null && selectedTimeSlot != null) {
                // Return the selected date and time slot to the caller
                val deliveryInfo = "${selectedDate?.date}, ${selectedTimeSlot?.timeRange}"

                // You can use a callback interface or shared ViewModel to pass data back
                (activity as? DeliverySlotSelectionListener)?.onDeliverySlotSelected(
                    selectedDate!!,
                    selectedTimeSlot!!
                )

                Toast.makeText(context, "Selected: $deliveryInfo", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(context, "Please select date and time", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateTimeSlots() {
        // In a real app, you might fetch time slots available for the selected date
        // For now, we'll just use the same set
        timeSlotAdapter.notifyDataSetChanged()
    }

    private fun generateDates(): List<DeliveryDate> {
        return listOf(
            DeliveryDate(1, "23 Nov 2024", true),
            DeliveryDate(2, "24 Nov 2024"),
            DeliveryDate(3, "25 Nov 2024")
        )
    }

    private fun generateTimeSlots(): List<TimeSlot> {
        return listOf(
            TimeSlot(1, "8 am - 12 pm"),
            TimeSlot(2, "12 pm - 4 pm", true),
            TimeSlot(3, "6 pm - 9 pm")
        )
    }

    // Interface for communication with host Activity/Fragment
    interface DeliverySlotSelectionListener {
        fun onDeliverySlotSelected(date: DeliveryDate, timeSlot: TimeSlot)
    }

    companion object {
        const val TAG = "DeliverySlotBottomSheet"

        fun newInstance(): DeliverySlotBottomSheet {
            return DeliverySlotBottomSheet()
        }
    }
}
