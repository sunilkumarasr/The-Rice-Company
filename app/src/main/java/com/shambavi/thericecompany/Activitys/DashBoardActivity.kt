package com.shambavi.thericecompany.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.CartCount
import com.gadiwalaUser.Models.MainResponse
import com.gadiwalaUser.services.DataManager
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Config.Preferences
import com.shambavi.thericecompany.home.HomeFragment
import com.shambavi.thericecompany.Fragments.CartFragment
import com.shambavi.thericecompany.orders.OrdersFragment
import com.shambavi.thericecompany.Fragments.ProfileFragment
import com.shambavi.thericecompany.Logins.LoginActivity
import com.shambavi.thericecompany.categories.CategoriesFragment
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityDashBoardBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashBoardActivity : AppCompatActivity() {

    val binding: ActivityDashBoardBinding by lazy {
        ActivityDashBoardBinding.inflate(layoutInflater)
    }
    var user_id=""

    private lateinit var bottomNavigationView: BottomNavigationView

    //exit
    private var isHomeFragmentDisplayed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Set the status bar color directly.
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        // This controls whether the status bar icons are light or dark.
        // Set to 'false' for dark status bars (to get light icons).
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        // Handle Window Insets to prevent content overlap
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply insets as padding to the root view.
            // This will push all content within binding.root away from the system bars.
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom)

            WindowInsetsCompat.CONSUMED
        }

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

        getUserStatus()

    }

    fun getUserStatus(){
        var user_id=  MyPref.getUser(applicationContext)

        // Obtain the DataManager instance
        //  dialog.showDialog(requireActivity(),false)

        val dataManager = DataManager.getDataManager()
        user_id?.let {
            dataManager.getUserStatus(object: Callback<MainResponse> {

                override fun onResponse(
                    call: Call<MainResponse>,
                    response: Response<MainResponse>
                ) {
                    //  dialog.closeDialog()
                    Log.e("response.body()","response.body() ${response.body()}")
                    if(response.body()?.Status ==true) {

                    }else
                    {
                        MyPref.clear(applicationContext)
                        Preferences.deleteSharedPreferences(applicationContext)
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                        Utils.showMessage(response.body()?.Message,applicationContext)

                    }
                    Log.e("response.body()","response.body() ")

                }

                override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                    // dialog.closeDialog()

                }

            }, it)
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
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
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

    override fun onBackPressed() {
        if (isHomeFragmentDisplayed) {
            exitDialog()
        } else {
            isHomeFragmentDisplayed = true
            // Navigate to HomeFragment
            binding.txtTitle.visibility = View.GONE
            loadFragment(HomeFragment())
        }
    }
    private fun exitDialog() {
        isHomeFragmentDisplayed = false
        val dialogBuilder = AlertDialog.Builder(this@DashBoardActivity)
        dialogBuilder.setTitle("Exit")
        dialogBuilder.setMessage("Are you sure want to exit this app?")
        dialogBuilder.setPositiveButton("Ok") { dialog, _ ->
            finishAffinity()
            dialog.dismiss()
        }
        dialogBuilder.setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        val b = dialogBuilder.create()
        b.show()
    }

}
