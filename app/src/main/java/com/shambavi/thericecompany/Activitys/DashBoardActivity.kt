package com.shambavi.thericecompany.Activitys

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shambavi.thericecompany.Config.Preferences
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.home.HomeFragment
import com.shambavi.thericecompany.Fragments.CartFragment
import com.shambavi.thericecompany.orders.OrdersFragment
import com.shambavi.thericecompany.Fragments.ProfileFragment
import com.shambavi.thericecompany.categories.CategoriesFragment
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityDashBoardBinding

class DashBoardActivity : AppCompatActivity() {

    val binding: ActivityDashBoardBinding by lazy {
        ActivityDashBoardBinding.inflate(layoutInflater)
    }

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        //login
        Preferences.saveStringValue(applicationContext, Preferences.LOGINCHECK, "Login")


        inits()
    }

    private fun inits() {
        //BottomNavigationView
        loadFragment(HomeFragment())
        bottomNavigationView = binding.navigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id. navigationHome-> {
                    binding.txtTitle.visibility = View.GONE
                    loadFragment(HomeFragment())
                    true
                }
                R.id. navigationProducts-> {
                    binding.txtTitle.visibility = View.VISIBLE
                    binding.txtTitle.setText("All Categories")
                    loadFragment(CategoriesFragment())
                    true
                }
                R.id. navigationOrders-> {
                    binding.txtTitle.visibility = View.VISIBLE
                    binding.txtTitle.setText("All Orders")
                    loadFragment(OrdersFragment())
                    true
                }
                R.id.navigationCart -> {
                    binding.txtTitle.visibility = View.VISIBLE
                    binding.txtTitle.setText("My Cart")
                    loadFragment(CartFragment())
                    true
                }
                R.id.navigationProfile -> {
                    binding.txtTitle.visibility = View.VISIBLE
                    binding.txtTitle.setText("Profile")
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentFrame, fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}