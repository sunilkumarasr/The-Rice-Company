package com.shambavi.thericecompany.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shambavi.thericecompany.Activitys.DashBoardActivity
import com.shambavi.thericecompany.R

class OrderSuccessActivity : AppCompatActivity() {
   lateinit var view:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        setContentView(R.layout.activity_order_success)
         view=findViewById<View>(R.id.card_continue)
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