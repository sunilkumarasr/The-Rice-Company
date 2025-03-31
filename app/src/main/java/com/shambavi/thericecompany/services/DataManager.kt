package com.gadiwalaUser.services


import android.util.Log
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.AddressDataMainRes
import com.gadiwalaUser.Models.BannersMainRes
import com.gadiwalaUser.Models.CartMainRes
import com.gadiwalaUser.Models.CategoryMainRes
import com.gadiwalaUser.Models.ContactDetailsMain
import com.gadiwalaUser.Models.FAQsMainRes
import com.gadiwalaUser.Models.LoginResponse
import com.gadiwalaUser.Models.MainResponse
import com.gadiwalaUser.Models.OTPResponse
import com.gadiwalaUser.Models.PrivacyDataMainRes
import com.gadiwalaUser.Models.ProductDetailsDataMinRes
import com.gadiwalaUser.Models.ProductMainRes
import com.gadiwalaUser.Models.SlotsMainRes
import com.gadiwalaUser.Models.SubCategoryMain
import com.gadiwalaUser.Models.UserDetailsMainRes
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.io.File
import java.util.concurrent.TimeUnit

class DataManager private constructor() {


    companion object {
        val ROOT_URL = "https://ritps.com/the_rice_company/"
        val APIKEY = "the_rice_company_7s736V2J2iB549214s40i3Lz77I0297L"

        private var dataManager: DataManager? = null
        @JvmStatic
        fun getDataManager(): DataManager {
            if (dataManager == null) {
                dataManager = DataManager()
            }
            return dataManager as DataManager
        }
    }

    private val retrofit: Retrofit

    init {
        val logging = HttpLoggingInterceptor()
// set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
// add your other interceptors …

        httpClient.callTimeout(5, TimeUnit.MINUTES)
        httpClient.readTimeout(5, TimeUnit.MINUTES)

// add logging as the last interceptor
        httpClient.addInterceptor(logging)

        val gson = GsonBuilder()
            .setLenient()
            .create()

        retrofit = Retrofit.Builder().baseUrl(ROOT_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
    }
    fun login(cb: Callback<LoginResponse>, mobile: String) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.login(APIKEY,mobile)
        call.enqueue(cb)
    }
    fun verifyOtp(cb: Callback<OTPResponse>, mobile: String, otp: String) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.verifyOtp(APIKEY,mobile,otp)
        call.enqueue(cb)
    }
    fun addAddress(cb: Callback<OTPResponse>, mobile: String,
                   user_id: String,
                   full_name: String,
                   type: String,
                   house_no: String,
                   floor: String,
                   landmark: String,
                   city_town: String,
                   state: String,
                   country: String,
                   zipcode: String,
                   ) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.addAddress(APIKEY,mobile,user_id,full_name,type,house_no,floor,landmark,city_town,state,country,zipcode)
        call.enqueue(cb)
    }


    fun getcategory(cb: Callback<CategoryMainRes>) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getcategory(APIKEY)
        call.enqueue(cb)
    }
    fun getcategorySubCat(cb: Callback<CategoryMainRes>) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getcategorySubCat(APIKEY)
        call.enqueue(cb)
    }

    fun getProducts(cb: Callback<ProductMainRes>,user_id:String) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getProducts(APIKEY,user_id)
        call.enqueue(cb)
    }
    fun getTopSellProducts(cb: Callback<ProductMainRes>,user_id:String) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getTopSellProducts(APIKEY,user_id)
        call.enqueue(cb)
    }
    fun submitEnquiery(cb: Callback<MainResponse>,name:String,email:String,phone:String,subject:String,message:String) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.submitEnquiery(APIKEY,name,email,phone,subject,message)
        call.enqueue(cb)
    }
    fun faqs(cb: Callback<FAQsMainRes>) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.faqs(APIKEY,"2")
        call.enqueue(cb)
    }

    fun contactDetails(cb: Callback<ContactDetailsMain>, ) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.contactDetails(APIKEY)
        call.enqueue(cb)
    }

    fun subCategory(cb: Callback<SubCategoryMain>,cat_id:String ) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.subCategory(APIKEY,cat_id)
        call.enqueue(cb)
    }

    fun productDetails(cb: Callback<ProductDetailsDataMinRes>, product_id:String,user_id:String ) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.productDetails(APIKEY,product_id,user_id)
        call.enqueue(cb)
    }

    fun getProductsByCat(cb: Callback<ProductMainRes>, sub_cat_id:String,user_id:String ) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getProductsByCat(APIKEY,sub_cat_id,user_id)
        call.enqueue(cb)
    }

    fun getProductsBySubCat(cb: Callback<ProductMainRes>, sub_cat_id:String,user_id:String ) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getProductsBySubCat(APIKEY,sub_cat_id,user_id)
        call.enqueue(cb)
    }

   /* fun saveAddress(cb: Callback<MainResponse>, sub_cat_id:String ) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.saveAddress(APIKEY,sub_cat_id)
        call.enqueue(cb)
    }*/
    fun getAddressDetails(cb: Callback<AddressDataMainRes>, user_id:String ) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getAddressDetails(APIKEY,user_id)
        call.enqueue(cb)
    }

    fun deleteAddress(cb: Callback<MainResponse>, address_id:String ) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.deleteAddress(APIKEY,address_id)
        call.enqueue(cb)
    }

    fun userDetails(cb: Callback<UserDetailsMainRes>, user_id:String ) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.userDetails(APIKEY,user_id)
        call.enqueue(cb)
    }

    fun updateProfile(cb: Callback<MainResponse>, user_id:String,phone:String,full_name:String,email:String ) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.updateProfile(APIKEY,user_id,phone,full_name,email)
        call.enqueue(cb)
    }
    fun updateProfileImage(cb: Callback<MainResponse>, user_id:String, profile_image:String) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.updateProfileImage(APIKEY,user_id,profile_image)
        call.enqueue(cb)
    }
    fun bannerList(cb: Callback<BannersMainRes>) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.bannerList(APIKEY)
        call.enqueue(cb)
    }

    fun privacyTermsData(cb: Callback<PrivacyDataMainRes>,pagename:String) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.privacyTermsData(APIKEY,pagename)
        call.enqueue(cb)
    }
    fun deleteAccount(cb: Callback<MainResponse>,user_d:String) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.deleteAccount(APIKEY,user_d)
        call.enqueue(cb)
    }
    fun getSlots(cb: Callback<SlotsMainRes>) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getSlots(APIKEY)
        call.enqueue(cb)
    }
    fun addCart(cb: Callback<MainResponse>,user_id:String,pid:String,attribut_id:String) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.addCart(APIKEY,user_id,pid,attribut_id,"1")
        call.enqueue(cb)
    }

    fun updateCart(cb: Callback<MainResponse>,user_id:String,cart_id:String,quantity:String) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.updateCart(APIKEY,user_id,cart_id,quantity)
        call.enqueue(cb)
    }
    fun getCart(cb: Callback<CartMainRes>, user_id:String) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getCart(APIKEY,user_id)
        call.enqueue(cb)
    }

    fun deleteProduct(cb: Callback<MainResponse>, userId: String, productId: String) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.deleteProduct(APIKEY,userId,productId)
        call.enqueue(cb)
    }
    fun getOrders(cb: Callback<MainResponse>, userId: String) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getOrders(APIKEY,userId)
        call.enqueue(cb)
    }
    fun placeOrder(cb: Callback<MainResponse>, userId: String,payment_id:String,address_id:String,amount:String,productId:String,qty:String,slotid:String,cartid:String) {
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.placeOrder(APIKEY,userId,payment_id,address_id,amount,productId,qty,slotid,cartid)
        call.enqueue(cb)
    }

}
