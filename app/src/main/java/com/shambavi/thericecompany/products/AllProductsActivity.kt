package com.shambavi.thericecompany.products

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bookiron.itpark.utils.MyPref
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.gadiwalaUser.Models.BannersMainRes
import com.gadiwalaUser.Models.CartMainRes
import com.gadiwalaUser.Models.MainResponse
import com.gadiwalaUser.Models.ProductMainRes
import com.gadiwalaUser.services.DataManager
import com.gadiwalaUser.services.DataManager.Companion.ROOT_URL
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Activitys.SearchActivity
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.cart.CheckOutActivity
import com.shambavi.thericecompany.categories.AllProductsAdapter
import com.shambavi.thericecompany.databinding.ActivityAllProductsBinding
import com.shambavi.thericecompany.filters.FilterBottomSheetFragment
import com.shambavi.thericecompany.listeners.ProductListener
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.text.append
import kotlin.text.indices

class AllProductsActivity : AppCompatActivity(),FilterBottomSheetFragment.FilterCallback,ProductListener
{
    lateinit var binding: ActivityAllProductsBinding
    lateinit var productsAdapter: AllProductsAdapter
    lateinit var recycler_all_products:RecyclerView
    var sid=""
    var sales=""
    var user_id=""
    var filter=""
    val imageList = ArrayList<SlideModel>() // Create image list


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // EdgeToEdge.enable(this);
        binding=ActivityAllProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)


        sid=intent.getStringExtra("sid").toString()
        sales=intent.getStringExtra("sales").toString()
        user_id=MyPref.getUser(applicationContext)
        recycler_all_products=findViewById(R.id.recycler_all_products)
        productsAdapter=AllProductsAdapter()
        recycler_all_products.adapter=productsAdapter
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.lnrCart.visibility=View.GONE
        binding.lnrCart.setOnClickListener {
            startActivity(Intent(applicationContext, CheckOutActivity::class.java))

        }
        binding.linearSearch.setOnClickListener {
            startActivityForResult(Intent(this@AllProductsActivity, SearchActivity::class.java),100)
        }

        binding.linearSearch.setOnClickListener {
            startActivity(Intent(applicationContext,SearchActivity::class.java))
        }
        binding.filterButton.setOnClickListener {
            showFilter()
        }
        productsAdapter.setListener(this)
        getProducts()
        getBanners()

    }
    private val filterLauncher = registerForActivityResult( ActivityResultContracts.StartActivityForResult() ) {
        result -> if (result.resultCode == RESULT_OK) {
        val filters = result.data?.getSerializableExtra("filters") as? Map<String, Set<String>> // Apply filters to your data
         } }
    private fun showFilter() {
        FilterBottomSheetFragment.newInstance() .show(supportFragmentManager, FilterBottomSheetFragment.TAG)
    }
            override fun onFiltersApplied(filters: Map<String, Set<String>>) {
                var s=filters.get("Price")
                if(s!=null&&s.size>0) {

                    val stringBuilder = StringBuilder()
                    for (i in s.indices) {
                        stringBuilder.append(s.elementAt(i))
                        if (i < s.size - 1) { // Add comma if not the last item
                            stringBuilder.append(",")
                        }
                    }
                    Log.e("filters.get(Price).toString()", "${stringBuilder}")
                    filter=stringBuilder.toString();
                    getProductsByPriceFilter()
                }
            }


    fun getProducts()
    {

        if(filter.isNotEmpty())
        {
            getProductsByPriceFilter()
            return
        }
        val dialog= CustomDialog(this@AllProductsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@AllProductsActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<ProductMainRes> {
            override fun onResponse(call: Call<ProductMainRes>, response: Response<ProductMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: ProductMainRes? = response.body()

                    // Handle the response

                    getCart()

                    if(model?.status == true)
                    {
                        if(model.products.size>0) {
                            productsAdapter.productList.clear()
                            productsAdapter.productList.addAll(model.products)

                            productsAdapter.notifyDataSetChanged()

                        }else
                        {
                            model?.message?.let { Utils.showMessage(it,this@AllProductsActivity) }
                        }

                    }else
                    {
                        model?.message?.let { Utils.showMessage(it,this@AllProductsActivity) }
                    }
                    checkData()
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
        if(sid.isEmpty())
            if(sales.isEmpty())
                dataManager.getProducts(otpCallback, user_id )
            else
                dataManager.getTopSellProducts(otpCallback,user_id)
        else
        dataManager.getProductsBySubCat(otpCallback,sid,user_id)
    }
    fun getProductsByPriceFilter()
    {

        val dialog= CustomDialog(this@AllProductsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@AllProductsActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<ProductMainRes> {
            override fun onResponse(call: Call<ProductMainRes>, response: Response<ProductMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: ProductMainRes? = response.body()

                    // Handle the response

                    getCart()

                    if(model?.status == true)
                    {
                        if(model.products.size>0) {
                            productsAdapter.productList.clear()
                            productsAdapter.productList.addAll(model.products)
                            productsAdapter.setListData(model.products)
                            productsAdapter.notifyDataSetChanged()

                        }else
                        {
                            productsAdapter.productList.clear()

                            productsAdapter.notifyDataSetChanged()
                            model?.message?.let { Utils.showMessage(it,this@AllProductsActivity) }
                        }

                    }else
                    {
                        model?.message?.let { Utils.showMessage(it,this@AllProductsActivity) }
                    }
                    checkData()
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


        dataManager.getProductsByPriceFilter(otpCallback, filter )
    }
    fun getBanners()
    {

        val dialog= CustomDialog(this@AllProductsActivity)
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
                            imageList.add(SlideModel("$ROOT_URL/${it.additional_image}", ScaleTypes.FIT))
                            println("Banners successfully: $ROOT_URL/${it.additional_image}")
                        }
                        binding.imageSlider.setImageList(imageList)

                    }
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")

                }

                if(imageList.isEmpty())
                    binding.imageSlider.visibility=View.GONE
                else
                    binding.imageSlider.visibility=View.VISIBLE
            }

            override fun onFailure(call: Call<BannersMainRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                //dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.bannerListByCat(otpCallback, sid )
    }

    fun getCart()
    {

        val dialog= CustomDialog(this@AllProductsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@AllProductsActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<CartMainRes> {
            override fun onResponse(call: Call<CartMainRes>, response: Response<CartMainRes>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: CartMainRes? = response.body()

                    // Handle the response

                    // model?.message?.let { Utils.showMessage(it,applicationContext) }
                  var count=  model!!.data.size
                    if(count>0)
                    {
                        binding.lnrCart.visibility=View.VISIBLE
                        binding.txtCount.setText("$count Item(s)")
                    }else{
                        binding.lnrCart.visibility=View.GONE
                    }

                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error

                    finish()

                }
            }

            override fun onFailure(call: Call<CartMainRes>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
                checkData()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.getCart(otpCallback, user_id  )
    }

    private fun checkData() {

        if(productsAdapter.itemCount>0)
        {
            binding.txtNoData.visibility= View.GONE
            binding.recyclerAllProducts.visibility= View.VISIBLE
        }else
        {
            binding.txtNoData.visibility= View.VISIBLE
            binding.recyclerAllProducts.visibility= View.GONE
        }
    }

    override fun addProduct(product_id: String, attribution_id: String) {

        addCart(product_id,attribution_id)

    }
    override fun deleteProduct(cart_id: String)  {

        val dialog= CustomDialog(this@AllProductsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@AllProductsActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                    //model?.Message?.let { Utils.showMessage(it,requireActivity()) }

                    getProducts()


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

        val dialog= CustomDialog(this@AllProductsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@AllProductsActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                    //model?.message?.let { Utils.showMessage(it,requireActivity()) }


                    getProducts()
                    println("OTP Sent successfully: ${model?.Message}")
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
        dataManager.updateCart(otpCallback,user_id ,cart_id,qnty.toString())
    }
    val startProductDetailsForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            getProducts()

        } else {
            // Log.d("MyActivity", "Result Canceled or Error: ${result.resultCode}")
        }
    }
    override fun onProductClick(productId: String) {
        val intent=Intent(this@AllProductsActivity, ProductDetailsActivity::class.java)
        intent.putExtra("product_id",productId)

        startProductDetailsForResult.launch(intent)
    }

    fun addCart(product_id: String, attribution_id: String)
    {

        val dialog= CustomDialog(this@AllProductsActivity)
        // Obtain the DataManager instance
        dialog.showDialog(this@AllProductsActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: MainResponse? = response.body()

                    // Handle the response

                    //model?.message?.let { Utils.showMessage(it,requireActivity()) }


                    getProducts()
                    println("OTP Sent successfully: ${model?.Message}")
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
        dataManager.addCart(otpCallback,user_id  ,product_id,attribution_id)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getProducts()
    }
}