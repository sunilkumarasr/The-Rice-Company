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
    @SerializedName("status"           ) var status          : String? = null,
    @SerializedName("profile_status"           ) var profile_status          : Int? = null

)


data class CategoryMainRes (

    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var categories    : ArrayList<Category> = arrayListOf()

)

data class Category (

    @SerializedName("id"             ) var id            : String? = null,
    @SerializedName("category"       ) var category      : String? = null,
    @SerializedName("category_image" ) var categoryImage : String? = null,
    @SerializedName("sub_category_list" ) var subCategoryList : ArrayList<SubCategory> = arrayListOf()


)


data class ProductMainRes (

    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var products    : ArrayList<Product> = arrayListOf()

)
data class Product (

    @SerializedName("id"                 ) var id                : String? = null,
    @SerializedName("product_id"         ) var productId         : String? = null,
    @SerializedName("category_id"        ) var categoryId        : String? = null,
    @SerializedName("sub_category_id"    ) var subCategoryId     : String? = null,
    @SerializedName("title"              ) var title             : String? = null,
    @SerializedName("category_id_name"              ) var category_id_name             : String? = null,
    @SerializedName("mrp_price"          ) var mrpPrice          : String? = null,
    @SerializedName("market_price"       ) var marketPrice       : String? = null,
    @SerializedName("our_price"          ) var ourPrice          : String? = null,
    @SerializedName("short_descriptions" ) var shortDescriptions : String? = null,
    @SerializedName("descriptions"       ) var descriptions      : String? = null,
    @SerializedName("specifications"     ) var specifications    : String? = null,
    @SerializedName("image"              ) var image             : String? = null,
    @SerializedName("meta_title"         ) var metaTitle         : String? = null,
    @SerializedName("meta_keywords"      ) var metaKeywords      : String? = null,
    @SerializedName("meta_description"   ) var metaDescription   : String? = null,
    @SerializedName("created_at"         ) var createdAt         : String? = null,
    @SerializedName("created_by"         ) var createdBy         : String? = null,
    @SerializedName("updated_at"         ) var updatedAt         : String? = null,
    @SerializedName("updated_by"         ) var updatedBy         : String? = null,
    @SerializedName("status"             ) var status            : String? = null

)

data class FAQsMainRes (

    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<FAQs> = arrayListOf()

)

data class FAQs (

    @SerializedName("id"         ) var id        : String? = null,
    @SerializedName("type_id"    ) var typeId    : String? = null,
    @SerializedName("module_id"  ) var moduleId  : String? = null,
    @SerializedName("question"   ) var question  : String? = null,
    @SerializedName("answer"     ) var answer    : String? = null,
    @SerializedName("created_at" ) var createdAt : String? = null,
    @SerializedName("created_by" ) var createdBy : String? = null,
    @SerializedName("updated_at" ) var updatedAt : String? = null,
    @SerializedName("updated_by" ) var updatedBy : String? = null,
    @SerializedName("status"     ) var status    : String? = null

)



data class ContactDetailsMain (

    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var date    : ArrayList<ContactDetails> = arrayListOf()

)

data class ContactDetails (

    @SerializedName("address" ) var address : String? = null,
    @SerializedName("phone"   ) var phone   : String? = null,
    @SerializedName("phone_2" ) var phone2  : String? = null,
    @SerializedName("email"   ) var email   : String? = null

)



data class SubCategoryMain (

    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<SubCategory> = arrayListOf()

)

data class SubCategory (

    @SerializedName("id"                 ) var id               : String? = null,
    @SerializedName("sub_category"       ) var subCategory      : String? = null,
    @SerializedName("sub_category_image" ) var subCategoryImage : String? = null

)



data class ProductDetailsDataMinRes (

    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : ProductDetailsData?    = ProductDetailsData()

)
data class ProductDetailsData (

    @SerializedName("product_details"   ) var productDetails   : ProductDetails?             = ProductDetails(),
    @SerializedName("product_attribute" ) var productAttribute : ArrayList<ProductAttribute> = arrayListOf(),
    @SerializedName("product_images"    ) var productImages    : ArrayList<ProductImages>    = arrayListOf()

)
data class ProductDetails (

    @SerializedName("id"                 ) var id                : String? = null,
    @SerializedName("product_id"         ) var productId         : String? = null,
    @SerializedName("category_id"        ) var categoryId        : String? = null,
    @SerializedName("sub_category_id"    ) var subCategoryId     : String? = null,
    @SerializedName("title"              ) var title             : String? = null,
    @SerializedName("mrp_price"          ) var mrpPrice          : String? = null,
    @SerializedName("market_price"       ) var marketPrice       : String? = null,
    @SerializedName("our_price"          ) var ourPrice          : String? = null,
    @SerializedName("short_descriptions" ) var shortDescriptions : String? = null,
    @SerializedName("descriptions"       ) var descriptions      : String? = null,
    @SerializedName("specifications"     ) var specifications    : String? = null,
    @SerializedName("image"              ) var image             : String? = null,
    @SerializedName("meta_title"         ) var metaTitle         : String? = null,
    @SerializedName("meta_keywords"      ) var metaKeywords      : String? = null,
    @SerializedName("meta_description"   ) var metaDescription   : String? = null,
    @SerializedName("created_at"         ) var createdAt         : String? = null,
    @SerializedName("created_by"         ) var createdBy         : String? = null,
    @SerializedName("updated_at"         ) var updatedAt         : String? = null,
    @SerializedName("updated_by"         ) var updatedBy         : String? = null,
    @SerializedName("status"             ) var status            : String? = null

)
data class ProductImages (

    @SerializedName("id"               ) var id              : String? = null,
    @SerializedName("product_id"       ) var productId       : String? = null,
    @SerializedName("additional_image" ) var additionalImage : String? = null

)
data class ProductAttribute (

    @SerializedName("id"              ) var id             : String? = null,
    @SerializedName("product_id"      ) var productId      : String? = null,
    @SerializedName("weight"          ) var weight         : String? = null,
    @SerializedName("prices"          ) var prices         : String? = null,
    @SerializedName("discount_prices" ) var discountPrices : String? = null,
    @SerializedName("weight_class_id" ) var weightClassId  : String? = null,
    @SerializedName("created_at"      ) var createdAt      : String? = null,
    @SerializedName("created_by"      ) var createdBy      : String? = null,
    @SerializedName("status"          ) var status         : String? = null

)


data class AddressDataMainRes (

    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<AddressData> = arrayListOf()

)
data class AddressData (

    @SerializedName("id"         ) var id        : String? = null,
    @SerializedName("user_id"    ) var userId    : String? = null,
    @SerializedName("full_name"  ) var fullName  : String? = null,
    @SerializedName("mobile"     ) var mobile    : String? = null,
    @SerializedName("type"       ) var type      : String? = null,
    @SerializedName("house_no"   ) var houseNo   : String? = null,
    @SerializedName("floor"      ) var floor     : String? = null,
    @SerializedName("landmark"   ) var landmark  : String? = null,
    @SerializedName("country"    ) var country   : String? = null,
    @SerializedName("state"      ) var state     : String? = null,
    @SerializedName("city_town"  ) var cityTown  : String? = null,
    @SerializedName("zip_code"   ) var zipCode   : String? = null,
    @SerializedName("created_at" ) var createdAt : String? = null,
    @SerializedName("created_by" ) var createdBy : String? = null,
    @SerializedName("updated_at" ) var updatedAt : String? = null,
    @SerializedName("updated_by" ) var updatedBy : String? = null,
    @SerializedName("status"     ) var status    : String? = null

)

data class UserDetailsMainRes (

    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<UserDetails> = arrayListOf()

)

data class UserDetails (

    @SerializedName("full_name" ) var fullName : String? = null,
    @SerializedName("phone"     ) var phone    : String? = null,
    @SerializedName("email"     ) var email    : String? = null

)



data class BannersMainRes (

    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<Banners> = arrayListOf()

)
data class Banners (

    @SerializedName("id"    ) var id    : String? = null,
    @SerializedName("image" ) var image : String? = null

)




data class PrivacyDataMainRes (

    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<PrivacyData> = arrayListOf()

)
data class PrivacyData (

    @SerializedName("id"                ) var id               : String? = null,
    @SerializedName("information_title" ) var informationTitle : String? = null,
    @SerializedName("description"       ) var description      : String? = null

)



data class SlotsMainRes (

    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<Slots> = arrayListOf()

)
data class Slots (

    @SerializedName("id"         ) var id        : String? = null,
    @SerializedName("date"       ) var date      : String? = null,
    @SerializedName("start_time" ) var startTime : String? = null,
    @SerializedName("end_time"   ) var endTime   : String? = null

)

data class AllCategoryResMain (

    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<Category> = arrayListOf()

)