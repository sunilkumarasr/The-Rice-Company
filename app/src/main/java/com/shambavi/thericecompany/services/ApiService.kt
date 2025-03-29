package com.gadiwalaUser.services



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


    @FormUrlEncoded
    @POST("api/save_address")
    fun addAddress(@Field("api_key") api_key: String,
                   @Field("mobile") mobile_number: String,
                   @Field("user_id") user_id: String,
                   @Field("full_name") full_name: String,
                   @Field("type") type: String,
                   @Field("house_no") house_no: String,
                   @Field("floor") floor: String,
                   @Field("landmark") landmark: String,
                   @Field("city_town") city: String,
                   @Field("state") state: String,
                   @Field("country") country: String,
                   @Field("zip_code") zipcode: String
    ): Call<OTPResponse>

    @FormUrlEncoded
    @POST("api/category_list")
    fun getcategory(@Field("api_key") api_key: String
    ): Call<CategoryMainRes>

 @FormUrlEncoded
    @POST("api/all_category_list")
    fun getcategorySubCat(@Field("api_key") api_key: String
    ): Call<CategoryMainRes>

    @FormUrlEncoded
    @POST("api/product_list")
    fun getProducts(@Field("api_key") api_key: String, @Field("user_id") id: String,
    ): Call<ProductMainRes>


    @FormUrlEncoded
    @POST("api/top_selling")
    fun getTopSellProducts(@Field("api_key") api_key: String, @Field("user_id") id: String,
    ): Call<ProductMainRes>

    @FormUrlEncoded
    @POST("api/enquiry_form")
    fun submitEnquiery(@Field("api_key") api_key: String,
                      @Field("name") name: String,
                      @Field("email") email: String,
                      @Field("phone") phone: String,
                      @Field("subject") subject: String,
                      @Field("message") message: String
    ): Call<MainResponse>


    @FormUrlEncoded
    @POST("api/faqs_list")
    fun faqs(@Field("api_key") api_key: String,
                      @Field("type_id") type_id: String,

    ): Call<FAQsMainRes>


    @FormUrlEncoded
    @POST("api/contact_details")
    fun contactDetails(@Field("api_key") api_key: String,

    ): Call<ContactDetailsMain>


    @FormUrlEncoded
    @POST("api/sub_category_list")
    fun subCategory(@Field("api_key") api_key: String,
                    @Field("category_id") cate_id: String

    ): Call<SubCategoryMain>

    @FormUrlEncoded
    @POST("api/get_product_details")
    fun productDetails(@Field("api_key") api_key: String,
                    @Field("product_id") cate_id: String, @Field("user_id") id: String,

    ): Call<ProductDetailsDataMinRes>

    @FormUrlEncoded
    @POST("api/get_product_cat_by")
    fun getProductsByCat(
        @Field("api_key") api_key: String,
        @Field("category_id") cat_id: String, @Field("user_id") id: String,
    ): Call<ProductMainRes>

    @FormUrlEncoded
    @POST("api/get_product_sub_cat_by")
    fun getProductsBySubCat(
        @Field("api_key") api_key: String,
        @Field("sub_category_id") sub_cat_id: String, @Field("user_id") id: String,
    ): Call<ProductMainRes>


   /* @FormUrlEncoded
    @POST("api/save_address")
    fun saveAddress(
        @Field("api_key") api_key: String,
        @Field("sub_category_id") sub_cat_id: String,
    ): Call<MainResponse>
*/

    @FormUrlEncoded
    @POST("api/user_address_details")
    fun getAddressDetails(
        @Field("api_key") api_key: String,
        @Field("user_id") user_id: String,
    ): Call<AddressDataMainRes>


    @FormUrlEncoded
    @POST("api/deleted_address")
    fun deleteAddress(
        @Field("api_key") api_key: String,
        @Field("id") id: String,
    ): Call<MainResponse>


    @FormUrlEncoded
    @POST("api/user_profile_details")
    fun userDetails(
        @Field("api_key") api_key: String,
        @Field("user_id") id: String,
    ): Call<UserDetailsMainRes>



    @FormUrlEncoded
    @POST("api/update_profile")
    fun updateProfile(
        @Field("api_key") api_key: String,
        @Field("user_id") user_id: String,
        @Field("phone") mobile_number: String,

        @Field("full_name") full_name: String,
        @Field("email") email: String,
    ): Call<MainResponse>

    @FormUrlEncoded
    @POST("api/update_profile_image")
    fun updateProfileImage(
        @Field("api_key") api_key: String,
        @Field("user_id") user_id: String,
        @Field("profile_image") profile_image: String,
    ): Call<MainResponse>

    @FormUrlEncoded
    @POST("api/banner_list")
    fun bannerList(
        @Field("api_key") api_key: String,
    ): Call<BannersMainRes>

    @FormUrlEncoded
    @POST("api/pages_list")
    fun privacyTermsData(
        @Field("api_key") api_key: String,
        @Field("page_name") pagename: String,//terms-and-condition,privacy-policy,shipping-policy,about-us
    ): Call<PrivacyDataMainRes>



    @FormUrlEncoded
    @POST("api/delete_account")
    fun deleteAccount(
        @Field("api_key") api_key: String,
        @Field("user_id") user_id: String,

    ): Call<MainResponse>

    @FormUrlEncoded
    @POST("api/delivery_slot")
    fun getSlots(
        @Field("api_key") api_key: String,

    ): Call<SlotsMainRes>

    @FormUrlEncoded
    @POST("api/add_to_cart")
    fun addCart(
        @Field("api_key") api_key: String,
        @Field("user_id") user_id: String,
        @Field("product_id") productId: String,
        @Field("attribute_id") attributeId: String,
        @Field("quantity") quantity: String,

    ): Call<MainResponse>

    @FormUrlEncoded
    @POST("api/update_cart")
    fun updateCart(
        @Field("api_key") api_key: String,
        @Field("user_id") user_id: String,
        @Field("cart_id") cart_id: String,
        @Field("quantity") quantity: String,

    ): Call<MainResponse>

    @FormUrlEncoded
    @POST("api/remove_from_cart")
    fun deleteProduct(
        @Field("api_key") api_key: String,
        @Field("user_id") user_id: String,
        @Field("cart_id") cart_id: String,

    ): Call<MainResponse>

    @FormUrlEncoded
    @POST("api/get_cart_details")
    fun getCart(
        @Field("api_key") api_key: String,
        @Field("user_id") user_id: String,

    ): Call<CartMainRes>
}