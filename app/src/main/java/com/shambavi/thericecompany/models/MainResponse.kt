package com.gadiwalaUser.Models

import com.google.gson.annotations.SerializedName

data class ProfileMainResponse(

    @SerializedName("Status"   ) var Status   : Boolean?            = null,
    @SerializedName("Message"  ) var Message  : String?             = null,
   // @SerializedName("Response" ) var profileModel : ArrayList<ProfileModel> = arrayListOf(),
    @SerializedName("code"     ) var code     : Int?                = null

)

data class MainResponse(

    @SerializedName("Status"   ) var Status   : Boolean?            = null,
    @SerializedName("Message"  ) var Message  : String?             = null,
    @SerializedName("code"     ) var code     : Int?                = null

)
data class LoginResponse (

    @SerializedName("status"  ) var status  : Boolean = false,
    @SerializedName("message" ) var message : String  = "",
    @SerializedName("otp"     ) var otp     : String?     = null

)


data class OTPResponse (

    @SerializedName("status"  ) var status  : Boolean = false,
    @SerializedName("message" ) var message : String  = "",
    @SerializedName("data"    ) var data    : UserModel?    = UserModel()

)


data class UserModel (

    @SerializedName("id"               ) var id              : String? = null,
    @SerializedName("users_id"         ) var usersId         : String? = null,
    @SerializedName("full_name"        ) var fullName        : String? = null,
    @SerializedName("frist_name"       ) var fristName       : String? = null,
    @SerializedName("last_name"        ) var lastName        : String? = null,
    @SerializedName("profile_image"    ) var profileImage    : String? = null,
    @SerializedName("email"            ) var email           : String? = null,
    @SerializedName("otp"              ) var otp             : String? = null,
    @SerializedName("phone"            ) var phone           : String? = null,
    @SerializedName("password"         ) var password        : String? = null,
    @SerializedName("password_int"     ) var passwordInt     : String? = null,
    @SerializedName("created_at"       ) var createdAt       : String? = null,
    @SerializedName("created_by"       ) var createdBy       : String? = null,
    @SerializedName("updated_at"       ) var updatedAt       : String? = null,
    @SerializedName("updated_by"       ) var updatedBy       : String? = null,
    @SerializedName("verification_key" ) var verificationKey : String? = null,
    @SerializedName("status"           ) var status          : String? = null

)

