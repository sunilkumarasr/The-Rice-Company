package com.shambavi.thericecompany.Activitys

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityMyAccountBinding

class MyAccountActivity : AppCompatActivity() {

    val binding: ActivityMyAccountBinding by lazy {
        ActivityMyAccountBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "My Account"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            //myAccountApi()
        }

        binding.imgEditProfile.setOnClickListener {
            ProfileDialog()
        }

    }

    private fun ProfileDialog() {
        val bottomSheetDialog = BottomSheetDialog(this@MyAccountActivity)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_editprofile, null)
        bottomSheetDialog.setContentView(view)
        val linearCancel = view.findViewById<TextView>(R.id.linearCancel)
        val linearUpload = view.findViewById<TextView>(R.id.linearUpload)
        linearCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        linearUpload.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }
}