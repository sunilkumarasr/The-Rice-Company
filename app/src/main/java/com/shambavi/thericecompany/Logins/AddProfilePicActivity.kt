package com.shambavi.thericecompany.Logins

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bookiron.itpark.utils.MyPref
import com.bumptech.glide.Glide
import com.gadiwalaUser.Models.OTPResponse
import com.gadiwalaUser.Models.ProductImages
import com.gadiwalaUser.Models.ProfileImgResp
import com.gadiwalaUser.Models.ProfileMainResponse
import com.gadiwalaUser.services.DataManager
import com.gadiwalaUser.services.DataManager.Companion.ROOT_URL
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.royalpark.gaadiwala_admin.views.CustomDialog
import com.shambavi.thericecompany.Activitys.DashBoardActivity
import com.shambavi.thericecompany.Activitys.MyAccountActivity
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityAddProfilePicBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddProfilePicActivity : AppCompatActivity() {

    val binding: ActivityAddProfilePicBinding by lazy {
        ActivityAddProfilePicBinding.inflate(layoutInflater)
    }
    var user_id=""
    var is_account=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        user_id= MyPref.getUser(applicationContext).toString()
        is_account=intent.getBooleanExtra("is_account",false)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        inits()
    }

    private fun inits() {
        if(is_account)
        {
            binding.txtSkip.visibility= View.GONE
            binding.lnrSpace.visibility= View.GONE
        }

        binding.txtBack.setOnClickListener {
            finish()
        }
        binding.txtSkip.setOnClickListener {
            MyPref.setProfileStatus(applicationContext,1)
            // if(model.data!!.profile_status==1){

            startActivity(Intent(this@AddProfilePicActivity, DashBoardActivity::class.java))
        }
        binding.linearVerify.setOnClickListener {
            if(is_account) {

            }else
            {
                val intent = Intent(this@AddProfilePicActivity, DashBoardActivity::class.java)
                startActivity(intent)
            }
            finish()
        }
        binding.lnrTakePhoto.setOnClickListener {
            checkCameraPermissionAndDispatchIntent()

        }
        binding.lnrPickPhoto.setOnClickListener {
            pickIMageIntent()

        }
    }

    private var latestTmpUri: Uri? = null // To store the URI of the image taken

    // Activity Result Launcher for Camera
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            // Image was captured successfully
            latestTmpUri?.let { uri: Uri ->
                // imageView.setImageURI(uri) // Display the image

                binding.imgProfile.setImageURI(uri)
                uploadImage(uri)
                // You can now process the image from this URI (e.g., upload, further manipulation)
            }
        } else {
            Log.e("Error","Error while capture the image")
            // Image capture failed or was cancelled

        }
    }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }
    private fun checkCameraPermissionAndDispatchIntent() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                dispatchTakePictureIntent()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // In an educational UI, explain to the user why your app needs this
                // permission for a specific feature. Then, proceed to request the
                // permission again.
                Toast.makeText(this, "Camera permission is needed to take photos.", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            else -> {
                // You can directly ask for the permission.
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
    // Activity Result Launcher for Permission Request
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, proceed to take a picture
            dispatchTakePictureIntent()
        } else {
            // Permission denied, show a message to the user
            Toast.makeText(this, "Camera permission is required to take pictures.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun dispatchTakePictureIntent() {
        try {
            val photoFile: File? = createImageFile()
            photoFile?.also {
                latestTmpUri = FileProvider.getUriForFile(
                    this,
                    "${applicationContext.packageName}.fileprovider", // Must match the authority in AndroidManifest.xml
                    it
                )
                takePictureLauncher.launch(latestTmpUri!!)
            }
        } catch (ex: IOException) {
            Toast.makeText(this, "Error creating image file: ${ex.message}", Toast.LENGTH_LONG).show()
        }
    }

    var hasNotificationPermissionGranted=false
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            askPermission()
                        } else {
                            // showSettingDialog()
                        }
                    }
                }
            } else {

                //pickIMageIntent()
            }
        }
    fun pickIMageIntent()
    {
        if (Build.VERSION.SDK_INT <= 32&& Build.VERSION.SDK_INT >=23) {
            if(!hasNotificationPermissionGranted) {
                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                return
            }
        }
        else{

        }


        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Image"), 100)
    }
    private fun askPermission() {


        MaterialAlertDialogBuilder(this, com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setTitle("Alert")
            .setMessage("Storage permission is required")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT <= 32) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun getFileFromUri(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            // Create a temporary file in your app's cache directory
            val tempFile = File(cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
            tempFile.createNewFile()
            val outputStream = FileOutputStream(tempFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==100) {
            val uri: Uri? = data?.getData()
            // Initialize bitmap
            // Initialize bitmap
            try {


                val returnCursor: Cursor = contentResolver.query(uri!!, null, null, null, null)!!
                val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                returnCursor!!.moveToFirst()
                uploadImage(uri)
                binding.imgProfile.setImageURI(uri)


                returnCursor!!.close()
                //uploadImage(uri)

            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("Image pick error", "image Pick Error ${e.printStackTrace()}")
            }
        }else
        {
            if(data==null)
                return

            val uri: Uri? = data?.getData()
            if(uri==null)
                return
            // Initialize bitmap
            // Initialize bitmap
            try {


                val returnCursor: Cursor = contentResolver.query(uri!!, null, null, null, null)!!
                val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                returnCursor!!.moveToFirst()
                uploadImage(uri)
                binding.imgProfile.setImageURI(uri)


                returnCursor!!.close()
                //uploadImage(uri)

            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("Image pick error", "image Pick Error ${e.printStackTrace()}")
            }
        }
    }
    fun uploadImage(uri: Uri){
        val dialog= CustomDialog(this@AddProfilePicActivity)
        dialog.showDialog(this@AddProfilePicActivity,false)

        val dataManager = DataManager.getDataManager()
        getFileFromUri(uri)?.let {
            dataManager.fileUpload(it,object: Callback<ProfileImgResp> {
                override fun onResponse(
                    call: Call<ProfileImgResp>,
                    response: Response<ProfileImgResp>
                ) {
                    dialog.closeDialog()
                    Log.e("response.body()","response.body() ${response.body()}")
                    if(response.body()?.status ==true)
                    {

                        MyPref.setUserPic(applicationContext,response.body()!!.imageUrl!!)
                        if(is_account)
                            startActivity(Intent(this@AddProfilePicActivity, MyAccountActivity::class.java))
                        else
                            startActivity(Intent(this@AddProfilePicActivity, DashBoardActivity::class.java))

                        finish()

                    }
                    Log.e("response.body()","response.body() ")

                }

                override fun onFailure(call: Call<ProfileImgResp>, t: Throwable) {
                    Log.e("response.body()","response.body() ${t.printStackTrace()}")
                    dialog.closeDialog()
                }

            },user_id)
        }
    }
}