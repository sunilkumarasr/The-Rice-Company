package com.shambavi.thericecompany.Logins

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bookiron.itpark.utils.MyPref
import com.gadiwalaUser.Models.OTPResponse
import com.gadiwalaUser.services.DataManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Activitys.DashBoardActivity
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityAddAddressBinding
import com.shambavi.thericecompany.databinding.ActivityLoginBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.Locale

class AddAddressActivity : AppCompatActivity() {

    val binding: ActivityAddAddressBinding by lazy {
        ActivityAddAddressBinding.inflate(layoutInflater)
    }
    var user_id=""
    var mobile=""
    var full_name=""
    var email=""
    var city=""
    var state=""
    var type=""
    var country=""
    var landmark=""
    var zipcode=""
    var flat=""
    var floor=""
    var area=""

    var lattitude=""
    var longitude=""
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermissionsAndGetLocation()
        val  apiKey=getString(R.string.MAP_KEY)
        Places.initialize(applicationContext, apiKey)

        user_id=MyPref.getUser(applicationContext).toString()
        mobile=MyPref.getMobile(applicationContext).toString()
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        type="Shop"
        inits()
    }

    private fun inits() {

        binding.txtSkip.setOnClickListener {
            MyPref.setProfileStatus(applicationContext,1)
            // if(model.data!!.profile_status==1){

            startActivity(Intent(this@AddAddressActivity, DashBoardActivity::class.java))
            finish()
        }
        binding.radioType.setOnCheckedChangeListener { group, checkedId ->

                if(checkedId==R.id.radio_home)
                {
                    type="Home"
                }
                else  if(checkedId==R.id.radio_shop)
                {
                    type="Shop"
                }else  if(checkedId==R.id.radio_other)
                {
                    type="Other"
                }


        }
        binding.linearVerify.setOnClickListener {
            full_name=binding.editFullName.text.toString()
            email=binding.editEmail.text.toString()
            flat=binding.editFlatNo.text.toString()
            floor=binding.editFloor.text.toString()
            area=binding.editArea.text.toString()
            landmark=binding.editLandmark.text.toString()
            city=binding.editCity.text.toString()
            state=binding.editState.text.toString()
            country=binding.editCountry.text.toString()
            zipcode=binding.editZipcode.text.toString()

            if(full_name.isEmpty()||email.isEmpty()||flat.isEmpty()||area.isEmpty()||city.isEmpty()||state.isEmpty()||country.isEmpty()||zipcode.isEmpty()||type.isEmpty())
            {
                Utils.showMessage("Please fill all details",applicationContext)
                return@setOnClickListener
            }
            addAddress()
        }
    }
    fun locationRequest()
    {
        val fields = listOf(
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.LOCATION,
            Place.Field.CURBSIDE_PICKUP,
            Place.Field.ADDRESS_COMPONENTS)

        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .setHint("Search Your Location")
            // .setLocationRestriction(rectangleBounds)

            .build(this)

        startAutocomplete.launch(intent)
    }
    var pickUpCity=""
    val TAG="UpdateAddressActivity"
    private val startAutocomplete =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            Log.e(TAG, "User canceled autocomplete ${result.resultCode}")

            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    var isCityFound=false;


                    pickUpCity=""
                    // binding.editPickLocation.setText(place.addressComponents.toString())

                    var street=""
                    var city=""
                    var state=""
                    var zipCode=""
                    var addressBuffer=StringBuilder(place.name+",")

                    place.addressComponents.asList().forEach {it->
                        val types =(it.types) as ArrayList<String>


                        if (types.contains("landmark") || types.contains("premise")) {
                            street = it.name;
                            addressBuffer.append(street+", ")
                        } else if (types.contains("sublocality_level_1")) {
                            state = it.name;
                            binding.editState.setText("$state")
                            addressBuffer.append(state+", ")
                        }else if (types.contains("locality")) {
                            city = it.name;
                            addressBuffer.append(city+", ")
                        } else if (types.contains("administrative_area_level_1")) {
                            state = it.name;
                            addressBuffer.append(state+", ")
                        } else if (types.contains("postal_code")) {
                            zipCode = it.name;
                            addressBuffer.append(zipCode)
                        }else if (types.contains("country")) {
                            country = it.name;
                            addressBuffer.append(country)
                        }
                    }
                    if(city.isNotEmpty())
                        binding.editCity.setText("$city")

                    if(state.isNotEmpty())
                        binding.editState.setText("$state")

                    if(zipCode.isNotEmpty())
                        binding.editZipcode.setText("$zipCode")

                    if(street.isNotEmpty())
                        binding.editLandmark.setText("$street")
                    if(country.isNotEmpty())
                        binding.editCountry.setText("$country")

                    if(street.isNotEmpty())
                        binding.editLandmark.setText("$street")

                    lattitude= place.latLng?.latitude.toString()
                    longitude= place.latLng?.longitude.toString()
                    // binding.editPickLocation.setText(addressBuffer.toString())

                    place!!.addressComponents.asList().forEach {
                        if(isCityFound)
                        {
                            return@forEach
                        }
                        it.types.forEach {locality->
                            if(locality.equals("locality"))
                            {
                                pickUpCity=it.name
                                isCityFound=true
                            }
                        }
                        Log.e("Place Location","Location ${it}")
                    }
                    Log.e(
                        TAG, "Place: ${place.name}, ${place.address} ${place.attributions}"
                    )
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.e(TAG, "User canceled autocomplete")
            }
        }
    fun addAddress() {
        val dialog= CustomDialog(applicationContext)
        // Obtain the DataManager instance
        dialog.showDialog(this@AddAddressActivity,false)
        val dataManager = DataManager.getDataManager()

        // Create a callback for handling the API response
        val otpCallback = object : Callback<OTPResponse> {
            override fun onResponse(call: Call<OTPResponse>, response: Response<OTPResponse>) {
                dialog.closeDialog()
                if (response.isSuccessful) {
                    val model: OTPResponse? = response.body()

                    // Handle the response

                    model?.message?.let { Utils.showMessage(it,applicationContext) }

                    if(model?.status == true)
                    {

                       /* val user=model.data
                        user!!?.let {
                            MyPref.setUser(applicationContext,
                                it.usersId!!,it.phone!!,it.fullName!!,it.email!!,it.profile_status!!)
                        }*/
                        MyPref.setProfileStatus(applicationContext,1)
                       // if(model.data!!.profile_status==1){

                        val intent=Intent(this@AddAddressActivity, AddProfilePicActivity::class.java)
                        intent.putExtra("is_account",false)
                        startActivity(intent)

                      /*  }else{
                            val intent = Intent(this@AddAddressActivity, AddAddressActivity::class.java)
                            startActivity(intent)


                        }*/

                        finish()

                    }
                    println("OTP Sent successfully: ${model?.message}")
                } else {
                    // Handle error
                    println("Failed to send OTP. ${response.message()}")
                }
            }

            override fun onFailure(call: Call<OTPResponse>, t: Throwable) {
                // Handle failure
                println("Failed to send OTP. ${t.message}")
                dialog.closeDialog()
            }
        }

        // Call the sendOtp function in DataManager
        dataManager.addAddress(otpCallback,mobile,user_id,full_name,type,flat,floor,area+","+landmark,city,state,country,zipcode,lattitude,longitude)
    }

    private fun checkPermissionsAndGetLocation() {
        when {
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                // Permissions are already granted, get the location
                getLastKnownLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                // Explain to the user why you need the permissions
                // You can show a dialog here before requesting again
                Toast.makeText(this, "Location permission is needed to get your current address.", Toast.LENGTH_LONG).show()
                // Then request
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
            else -> {
                // Directly request for the permissions
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // This check is technically redundant here due to checkPermissionsAndGetLocation,
            // but good practice for direct calls to FusedLocationProviderClient methods.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // Location found, now get the address
                    getAddressFromLocation(location)
                } else {
                    // You might want to request location updates here if lastLocation is null
                }
            }
            .addOnFailureListener { e ->

            }
    }

    private fun getAddressFromLocation(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            // Using geocoder.getFromLocation() which is synchronous.
            // For API level 33 and above, you can use the asynchronous version:
            // geocoder.getFromLocation(location.latitude, location.longitude, 1) { addresses -> ... }
            // For broader compatibility, the synchronous version is used here.
            // Consider running it on a background thread if it causes UI freezes on older devices.

            val addresses: List<Address>? = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1 // maxResults
            )

            if (!addresses.isNullOrEmpty()) {
                val address: Address = addresses[0]

                city = address.locality ?: "" // City
                state = address.adminArea ?: "" // State
                zipcode = address.postalCode ?: "" // Pincode/Zipcode
                country = address.countryName ?: ""
                val knownName = address.featureName ?: "" // e.g., Street Name or POI
                val fullAddress = address.getAddressLine(0) // Usually the full address

                binding.editCity.setText("$city")
                binding.editState.setText("$state")
                binding.editZipcode.setText("$zipcode")
                binding.editCountry.setText("$country")
                binding.editArea.setText("$fullAddress")
                binding.editFlatNo.setText("$knownName")

                area=fullAddress
                flat=knownName

                lattitude=location.latitude.toString()
                longitude=location.longitude.toString()
                val addressText = StringBuilder()
                addressText.append("Full Address: $fullAddress\n")
                addressText.append("Street/Feature: $knownName\n")
                addressText.append("City: $city\n")
                addressText.append("State: $state\n")
                addressText.append("Pincode: $zipcode\n")
                addressText.append("Country: $country\n\n")
                addressText.append("Lat: ${location.latitude}, Lon: ${location.longitude}")


                Log.d("AddressDebug", "City: $city, State: $state, Pincode: $zipcode")
                Log.d("AddressDebug", "Full Address Object: $address")


                // Update your EditText fields if needed:
                // binding.editCity.setText(city)
                // binding.editState.setText(state)
                // binding.editZipcode.setText(postalCode)
                // binding.editCountry.setText(country)
                // ... and so on for other address components you need.

            } else {
                Toast.makeText(this, "No address found.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            // Network or other I/O issues.
            Log.e("GeocoderError", "Service not available", e)
        } catch (e: IllegalArgumentException) {
            // Invalid latitude or longitude.
        }
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            var allPermissionsGranted = true
            permissions.entries.forEach {
                if (!it.value) {
                    allPermissionsGranted = false
                }
            }
            if (allPermissionsGranted) {
                // Permissions Granted
                getLastKnownLocation()
            } else {
                // Permissions Denied
                Utils.showMessage( "Location permissions are required to fetch the address.", applicationContext)
            }
        }
}