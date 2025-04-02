package com.shambavi.thericecompany.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shambavi.thericecompany.Activitys.AboutUsActivity
import com.shambavi.thericecompany.Activitys.ContactUsActivity
import com.shambavi.thericecompany.Activitys.EnquiryFormActivity
import com.shambavi.thericecompany.Activitys.FAQSActivity
import com.shambavi.thericecompany.Activitys.HelpAndSupportActivity
import com.shambavi.thericecompany.Activitys.MyAccountActivity
import com.shambavi.thericecompany.Activitys.PrivacyPolicyActivity
import com.shambavi.thericecompany.Activitys.ProductCancelationActivity
import com.shambavi.thericecompany.Activitys.SavedAddressActivity
import com.shambavi.thericecompany.Activitys.TermsAndConditionsActivity
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.cart.AddressListActivity
import com.shambavi.thericecompany.databinding.FragmentHomeBinding
import com.shambavi.thericecompany.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() ,View.OnClickListener {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {


        binding.relativeMyAccount.setOnClickListener(this)
        binding.relativeSavedAddress.setOnClickListener(this)
        binding.relativeProductCancelation.setOnClickListener(this)
        binding.relativeAboutUS.setOnClickListener(this)
        binding.relativePrivacyPolicy.setOnClickListener(this)
        binding.relativeTermsAndConditions.setOnClickListener(this)
        binding.relativeHelpAndSupport.setOnClickListener(this)
        binding.relativeContactUS.setOnClickListener(this)
        binding.relativeFAQS.setOnClickListener(this)
        binding.relativeEnquiryForm.setOnClickListener(this)
        binding.relativeLogout.setOnClickListener(this)

    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.relativeMyAccount -> {
                startActivity(Intent(requireActivity(), MyAccountActivity::class.java))
            }
            R.id.relativeSavedAddress -> {
                startActivity(Intent(requireActivity(), AddressListActivity::class.java))
            }
            R.id.relativeProductCancelation -> {
                startActivity(Intent(requireActivity(), ProductCancelationActivity::class.java))
            }
            R.id.relativeAboutUS -> {
                startActivity(Intent(requireActivity(), AboutUsActivity::class.java))
            }
            R.id.relativePrivacyPolicy -> {
                val intent=Intent(requireActivity(), PrivacyPolicyActivity::class.java)
                intent.putExtra("isOtherDetails","")

                startActivity(intent)
            }
            R.id.relativeTermsAndConditions -> {
                startActivity(Intent(requireActivity(), TermsAndConditionsActivity::class.java))
            }
            R.id.relativeHelpAndSupport -> {
                startActivity(Intent(requireActivity(), HelpAndSupportActivity::class.java))
            }
            R.id.relativeContactUS -> {
                startActivity(Intent(requireActivity(), ContactUsActivity::class.java))
            }
            R.id.relativeFAQS -> {
                startActivity(Intent(requireActivity(), FAQSActivity::class.java))
            }
            R.id.relativeEnquiryForm -> {
                startActivity(Intent(requireActivity(), EnquiryFormActivity::class.java))
            }
            R.id.relativeLogout -> {
                LogoutDialog()
            }
        }
    }


    private fun LogoutDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_logout, null)
        bottomSheetDialog.setContentView(view)
        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)
        val buttonOk = view.findViewById<Button>(R.id.buttonOk)
        buttonCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        buttonOk.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

}