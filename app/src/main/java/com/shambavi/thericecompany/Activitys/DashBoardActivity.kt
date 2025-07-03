package com.shambavi.thericecompany.Activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.CartCount
import com.gadiwalaUser.services.DataManager
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Config.Preferences
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.home.HomeFragment
import com.shambavi.thericecompany.Fragments.CartFragment
import com.shambavi.thericecompany.orders.OrdersFragment
import com.shambavi.thericecompany.Fragments.ProfileFragment
import com.shambavi.thericecompany.Logins.LoginActivity
import com.shambavi.thericecompany.categories.CategoriesFragment
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityDashBoardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashBoardActivity : AppCompatActivity() {

    val binding: ActivityDashBoardBinding by lazy {
        ActivityDashBoardBinding.inflate(layoutInflater)
    }
    var user_id=""

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        //login
        Preferences.saveStringValue(applicationContext, Preferences.LOGINCHECK, "Login")
        user_id= MyPref.getUser(applicationContext)


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

    fun setNotification(cartCount: Int)
    {

        var isVisible=false
        if(cartCount>0)
            isVisible=true

        val navView: BottomNavigationView? = binding.navigationView
        val menuItem = navView!!.menu.findItem(R.id.navigationCart) // Replace with your menu item ID

        // Create a BadgeDrawable
        val badgeDrawable = BadgeDrawable.create(this) // 'this' refers to the Context

        // Set the count for the badge
        badgeDrawable.number = 5 // Set your notification count here

        // Attach the badge to the menu item
        // You can specify gravity, horizontal offset, and vertical offset if needed
        badgeDrawable.isVisible = isVisible // Make the badge visible
        menuItem?.let {
            binding.navigationView!!.getOrCreateBadge(it.itemId).apply {
                number = cartCount // Set your notification count here
                isVisible = isVisible // Make the badge visible
            }

            val badge = binding.navigationView!!.getBadge(it.itemId) // Use getBadge to get existing badge
badge!!.isVisible=isVisible
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentFrame, fragment)
        transaction.commit()
        getCartCount()
    }
    fun loadFragment() {
        bottomNavigationView.setSelectedItemId(R.id.navigationProducts);

        binding.txtTitle.visibility = View.VISIBLE
        binding.txtTitle.setText("All Categories")

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentFrame, CategoriesFragment())
        transaction.commit()
    }

    fun finishAll()
    {
        val intent= Intent(applicationContext, LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }


    fun getCartCount()
    {

        val dialog= CustomDialog(this@DashBoardActivity)
        // Obtain the DataManager instance
        // dialog.showDialog(activity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<CartCount> {
            override fun onResponse(call: Call<CartCount>, response: Response<CartCount>) {
                // dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: CartCount? = response.body()

                    // Handle the response

                    ////model?.message?.let { Utils.showMessage(it,requireActivity()) }

                    if(model?.status == true)
                    {

                        if(model.cartCount!=null&& model.cartCount!! >0)
                            setNotification(model.cartCount!!)
                        else
                            setNotification(0)


                    }else
                    {
                        setNotification(0)
                    }
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<CartCount>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                // dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.cartCount(otpCallback,user_id)
    }
}