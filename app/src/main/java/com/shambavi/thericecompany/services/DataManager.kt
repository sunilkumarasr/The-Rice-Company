package com.gadiwalaUser.services


import android.util.Log
import com.gadiwalaUser.Models.LoginResponse
import com.gadiwalaUser.Models.MainResponse
import com.gadiwalaUser.Models.OTPResponse
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

}
