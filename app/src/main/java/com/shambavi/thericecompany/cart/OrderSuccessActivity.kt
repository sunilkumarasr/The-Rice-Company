package com.shambavi.thericecompany.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shambavi.thericecompany.Activitys.DashBoardActivity
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.utils.Utils

class OrderSuccessActivity : AppCompatActivity() {
   lateinit var view:View
   var saved="0"
   var OrderID=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        setContentView(R.layout.activity_order_success)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        view=findViewById<View>(R.id.card_continue)
       var  txt_saved=findViewById<TextView>(R.id.txt_saved)
       var  tvOrderID=findViewById<TextView>(R.id.tvOrderID)
        saved=intent.getStringExtra("saved").toString()
        OrderID=intent.getStringExtra("OrderID").toString()
        txt_saved.setText("Total Saved ${Utils.RUPEE_SYMBOL} $saved")
        tvOrderID.setText("Your Order ID: $OrderID")
        view.setOnClickListener {
            var intent=Intent(applicationContext,DashBoardActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        view.performClick()
    }
}