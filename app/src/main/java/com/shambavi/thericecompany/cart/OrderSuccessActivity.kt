package com.shambavi.thericecompany.cart

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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
        val views= LayoutInflater.from(applicationContext).inflate(R.layout.activity_order_success,null)
        setContentView(views)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.VANILLA_ICE_CREAM)
        {
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

            // 2. Handle Window Insets to prevent content overlap
            ViewCompat.setOnApplyWindowInsetsListener(views) { view, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

                // Apply insets as padding to the root view.
                // This will push all content within binding.root away from the system bars.
                view.setPadding(insets.left, 0, insets.right, insets.bottom)

                // If specific views still overlap or need different behavior (e.g., a Toolbar
                // intended to sit behind a transparent status bar), you'll need to apply
                // padding or margins more selectively to those specific views or their containers.
                // For instance, to only pad the top of your contentFrame and bottom of navigationView:
                // binding.contentFrame.setPadding(insets.left, insets.top, insets.right, binding.contentFrame.paddingBottom)
                // binding.navigationView.setPadding(binding.navigationView.paddingLeft, binding.navigationView.paddingTop, binding.navigationView.paddingRight, insets.bottom)


                WindowInsetsCompat.CONSUMED
            }

        }
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