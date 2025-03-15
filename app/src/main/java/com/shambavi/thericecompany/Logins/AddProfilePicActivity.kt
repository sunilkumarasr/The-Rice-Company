package com.shambavi.thericecompany.Logins

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bookiron.itpark.utils.MyPref
import com.shambavi.thericecompany.Activitys.DashBoardActivity
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityAddProfilePicBinding

class AddProfilePicActivity : AppCompatActivity() {

    val binding: ActivityAddProfilePicBinding by lazy {
        ActivityAddProfilePicBinding.inflate(layoutInflater)
    }
    var user_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        user_id= MyPref.getUser(applicationContext).toString()
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        inits()
    }

    private fun inits() {
        binding.linearVerify.setOnClickListener {
            val intent = Intent(this@AddProfilePicActivity, DashBoardActivity::class.java)
            startActivity(intent)
        }
    }
}