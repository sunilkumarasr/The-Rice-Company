package com.gadiwalaUser.services



import com.gadiwalaUser.Models.LoginResponse
import com.gadiwalaUser.Models.MainResponse
import com.gadiwalaUser.Models.OTPResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @FormUrlEncoded
    @POST("api/login")
    fun login(@Field("api_key") api_key: String,@Field("phone") mobile_number: String): Call<LoginResponse>

    @FormUrlEncoded
    @POST("api/otp_verify")
    fun verifyOtp(@Field("api_key") api_key: String,@Field("phone") mobile_number: String,@Field("otp") otp: String): Call<OTPResponse>



}