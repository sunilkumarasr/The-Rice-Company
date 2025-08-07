package com.shambavi.thericecompany.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookiron.itpark.utils.MyPref
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.gadiwalaUser.Models.BannersMainRes
import com.gadiwalaUser.Models.CategoryMainRes
import com.gadiwalaUser.Models.MainResponse
import com.gadiwalaUser.Models.ProductMainRes
import com.gadiwalaUser.services.DataManager
import com.gadiwalaUser.services.DataManager.Companion.ROOT_URL
import com.royalit.motherchoice.utils.NetWorkConection
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Activitys.DashBoardActivity
import com.shambavi.thericecompany.Activitys.NotificationsActivity
import com.shambavi.thericecompany.Activitys.SearchActivity
import com.shambavi.thericecompany.categories.AllProductsAdapter
import com.shambavi.thericecompany.databinding.FragmentHomeBinding
import com.shambavi.thericecompany.listeners.ProductListener
import com.shambavi.thericecompany.products.AllProductsActivity
import com.shambavi.thericecompany.products.ProductDetailsActivity
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class HomeFragment : Fragment(), ProductListener {

    private lateinit var binding: FragmentHomeBinding
lateinit var categoryAdapter: CategoryAdapter
lateinit var productsAdapter: AllProductsAdapter
lateinit var topSellingAdapter: AllProductsAdapter
var user_id=""
    val imageList = ArrayList<SlideModel>() // Create image list

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }
    val startProductDetailsForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            getProducts()
            getTopSellProducts()
              } else {
           // Log.d("MyActivity", "Result Canceled or Error: ${result.resultCode}")
        }
    }
    override fun onProductClick(productId: String) {
        val intent=Intent(requireActivity(), ProductDetailsActivity::class.java)
        intent.putExtra("product_id",productId)

        startProductDetailsForResult.launch(intent)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    fun openWhatsAppChat( toNumber: String, message: String) {
        val url = "https://api.whatsapp.com/send?phone=$toNumber&text=$message"
        try {
            requireContext().packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            requireContext().startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) })
        } catch (e: PackageManager.NameNotFoundException) {
            requireContext().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }


    }/*
    fun makeWhatsAppCall(context: Context, phoneNumberWithCountryCode: String) {
        val uri = Uri.parse("whatsapp://call?number=$phoneNumberWithCountryCode") // Or sometimes "tel:" might try to use WhatsApp if it's the default dialer for such links.
        val intent = Intent(Intent.ACTION_VIEW, uri) // Or Intent.ACTION_DIAL for a more generic approach

        // Check if WhatsApp is installed
        val packageManager: PackageManager = context.packageManager
        if (intent.resolveActivity(packageManager) != null) {
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Could not initiate WhatsApp call.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show()
        }
    }*/

    private fun init() {

        user_id=MyPref.getUser(requireActivity().applicationContext)
        categoryAdapter=CategoryAdapter()
        productsAdapter=AllProductsAdapter()
        topSellingAdapter=AllProductsAdapter()

        binding.txtName.setText("${MyPref.getName(requireActivity().applicationContext)}")
        productsAdapter.setListener(this)
        topSellingAdapter.setListener(this)

        binding.imgWhatsapp.setOnClickListener {

            openWhatsAppChat("919966484556","@Rice Bazaar")
        }
        if(!NetWorkConection.isNEtworkConnected(requireActivity()))
        {
            AlertDialog.Builder(requireActivity())
                .setTitle("Alert!")
                .setMessage("No Network available")
                .show()
        }
        binding.recyclerTopCatgories.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerProducts.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerProductsTopSelling.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerTopCatgories.adapter=categoryAdapter
        binding.recyclerProducts.adapter=productsAdapter
        binding.recyclerProductsTopSelling.adapter=topSellingAdapter

        binding.imgNotification.setOnClickListener {
            startActivity(Intent(requireActivity(), NotificationsActivity::class.java))
        }
        binding.linearSearch.setOnClickListener {
            startActivityForResult(Intent(requireActivity(), SearchActivity::class.java),100)
        }
        binding.txtSeeAllProducts.setOnClickListener {
            val intent=Intent(context,AllProductsActivity::class.java)
            intent.putExtra("pid","")
            intent.putExtra("sid","")
            intent.putExtra("sales","")

            startActivity(intent)
        }
        binding.txtseealltopselling.setOnClickListener {
            val intent=Intent(context,AllProductsActivity::class.java)
            intent.putExtra("pid","")
            intent.putExtra("sid","")
            intent.putExtra("sales","sales")
            startActivity(intent)
        }
        binding.lnrSeeCategories.setOnClickListener {
            (activity as DashBoardActivity).loadFragment()

           /* val intent=Intent(context,AllProductsActivity::class.java)
            intent.putExtra("pid","")
            intent.putExtra("sid","")
            startActivity(intent)*/
        }
        getCategories()
        getProducts()
        getTopSellProducts()
        getBanners()
    }

    fun getBanners()
    {

        val dialog= CustomDialog(requireActivity())
        // Obtain the DataManager instance
       // dialog.showDialog(activity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<BannersMainRes> {
            override fun onResponse(call: Call<BannersMainRes>, response: Response<BannersMainRes>) {
               // dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: BannersMainRes? = response.body()

                    // Handle the response

                    //model?.message?.let { Utils.showMessage(it,requireActivity()) }

                    if(model?.status == true)
                    {
                        imageList.clear()
                        model.data.forEach {
                            imageList.add(SlideModel("${ROOT_URL}/${it.image}", ScaleTypes.FIT))
                            println("Banners successfully: ${ROOT_URL}/${it.image}")
                        }
                        binding.imageSlider.setImageList(imageList)

                    }
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<BannersMainRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                //dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.bannerList(otpCallback)
    }
    fun getCategories()
    {

        val dialog= CustomDialog(requireActivity())
        // Obtain the DataManager instance
       // dialog.showDialog(activity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<CategoryMainRes> {
            override fun onResponse(call: Call<CategoryMainRes>, response: Response<CategoryMainRes>) {
               // dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: CategoryMainRes? = response.body()

                    // Handle the response

                    //model?.message?.let { Utils.showMessage(it,requireActivity()) }

                    if(model?.status == true)
                    {
                        if(model.categories.size>0) {
                            categoryAdapter.categoryList.clear()
                            categoryAdapter.categoryList.addAll(model.categories)
                            categoryAdapter.notifyDataSetChanged()
                            return
                        }

                    }
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<CategoryMainRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
               // dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.getcategory(otpCallback)
    }
    fun getProducts()
    {

        (requireActivity() as DashBoardActivity) .getCartCount()
        val dialog= CustomDialog(requireActivity())
        // Obtain the DataManager instance
       // dialog.showDialog(activity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<ProductMainRes> {
            override fun onResponse(call: Call<ProductMainRes>, response: Response<ProductMainRes>) {
               // dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: ProductMainRes? = response.body()

                    // Handle the response

                    ////model?.message?.let { Utils.showMessage(it,requireActivity()) }

                    if(model?.status == true)
                    {
                        if(model.products.size>0) {
                            productsAdapter.productList.clear()
                            productsAdapter.productList.addAll(model.products)
                            productsAdapter.notifyDataSetChanged()
                            return
                        }

                    }
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<ProductMainRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
               // dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.getProducts(otpCallback,user_id)
    }
    fun getTopSellProducts()
    {

        val dialog= CustomDialog(requireActivity())
        // Obtain the DataManager instance
       // dialog.showDialog(activity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<ProductMainRes> {
            override fun onResponse(call: Call<ProductMainRes>, response: Response<ProductMainRes>) {
                //dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: ProductMainRes? = response.body()

                    // Handle the response

                    //model?.message?.let { Utils.showMessage(it,requireActivity()) }

                    if(model?.status == true)
                    {
                        if(model.products.size>0) {
                            topSellingAdapter.productList.clear()
                            topSellingAdapter.productList.addAll(model.products)
                            topSellingAdapter.notifyDataSetChanged()
                            return
                        }

                    }
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<ProductMainRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.getTopSellProducts(otpCallback,user_id)
    }

    override fun addProduct(product_id: String,attribution_id:String) {
        addCart(product_id,attribution_id)

    }

    override fun deleteProduct(cart_id: String)  {

        val dialog= CustomDialog(requireActivity())
        // Obtain the DataManager instance
       // dialog.showDialog(requireActivity(),false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
              //  dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                    //model?.Message?.let { Utils.showMessage(it,requireActivity()) }

                    getProducts()
                    getTopSellProducts()

                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.deleteProduct(otpCallback, user_id,cart_id  )
    }

    override fun updateProduct(cart_id: String, qnty: Int) {


        val dialog= CustomDialog(requireActivity())
        // Obtain the DataManager instance
        //dialog.showDialog(activity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
              //  dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                    //model?.message?.let { Utils.showMessage(it,requireActivity()) }


                    getProducts()
                    getTopSellProducts()
                    println("OTP Sent successfully: ${model?.Message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }
            }

            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
              //  dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.updateCart(otpCallback,user_id ,cart_id,qnty.toString())
    }
     fun addCart(product_id: String, attribution_id: String)
         {

             val dialog= CustomDialog(requireActivity())
             // Obtain the DataManager instance
            // dialog.showDialog(activity,false)
             val dataManager = DataManager.getDataManager()

             // Create a callback for handling the API response
             val otpCallback = object : Callback<MainResponse> {
                 override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                   //  dialog.closeDialog()
                     if (response.isSuccessful) {
                         val model: MainResponse? = response.body()

                         // Handle the response

                         //model?.message?.let { Utils.showMessage(it,requireActivity()) }


                            getProducts()
                         getTopSellProducts()
                         println("OTP Sent successfully: ${model?.Message}")
                     } else {
                         // Handle error
                         println("Failed to send OTP. ${response.message()}")

                     }
                 }

                 override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                     // Handle failure
                     println("Failed to send OTP. ${t.message}")
                   //  dialog.closeDialog()
                 }
             }

             // Call the sendOtp function in DataManager
             dataManager.addCart(otpCallback,user_id  ,product_id,attribution_id)
         }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getProducts()
        getTopSellProducts()
    }
}