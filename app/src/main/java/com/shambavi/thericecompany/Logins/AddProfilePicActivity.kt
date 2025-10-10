package com.shambavi.thericecompany.Logins

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.exifinterface.media.ExifInterface
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
import com.shambavi.thericecompany.Config.Preferences
import com.shambavi.thericecompany.Config.ViewController
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityAddProfilePicBinding
import com.shambavi.thericecompany.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
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
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply insets as padding to the root view.
            // This will push all content within binding.root away from the system bars.
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom)

            WindowInsetsCompat.CONSUMED
        }
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
                val file= getCompressedImageFile(uri)
                if (file != null) {
                    uploadImage(file,uri)
                }

                // You can now process the image from this URI (e.g., upload, further manipulation)
            }
        } else {
            Log.e("Error","Error while capture the image")
            // Image capture failed or was cancelled

        }
    }

    private fun getCompressedImageFile(uri: Uri, quality: Int = 80, maxWidth: Int = 512, maxHeight: Int = 512): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null

            // 1. Decode bitmap with inJustDecodeBounds=true to check dimensions
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream.close() // Close and reopen to reset the stream

            // 2. Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)

            // 3. Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false
            var bitmap = contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, options) } ?: return null

            // 4. Handle EXIF Orientation (Important for camera photos)
            contentResolver.openInputStream(uri)?.use { exifStream ->
                val exif = ExifInterface(exifStream)
                val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL)
                val matrix = Matrix()
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                    // Add other cases like flip horizontal/vertical if needed
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            }


            // 5. Create a byte array output stream for compression
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)

            // 6. Create a temporary file and write the compressed data
            val tempFile = File(cacheDir, "compressed_image_${System.currentTimeMillis()}.jpg")
            tempFile.createNewFile()
            val fileOutputStream = FileOutputStream(tempFile)
            fileOutputStream.write(byteArrayOutputStream.toByteArray())
            fileOutputStream.close()
            byteArrayOutputStream.close()

            bitmap.recycle() // Recycle bitmap to free memory

            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ImageCompression", "Error compressing image: ${e.message}")
            null // Fallback to original method if compression fails, or handle error
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun uploadImage(file:File,uri:Uri){


        val dialog= CustomDialog(this@AddProfilePicActivity)
        dialog.showDialog(this@AddProfilePicActivity,false)
        val dataManager = DataManager.getDataManager()
        file?.let {
            dataManager.fileUpload(it,object: Callback<ProfileImgResp> {
                override fun onResponse(
                    call: Call<ProfileImgResp>,
                    response: Response<ProfileImgResp>
                ) {
                    dialog.closeDialog()
                    Log.e("response.body()","response.body() ${response.body()}")
                    if(response.body()?.status ==true)
                    {
                        val model: ProfileImgResp? = response.body()



                    }
                    Log.e("response.body()","response.body() ")

                }

                override fun onFailure(call: Call<ProfileImgResp>, t: Throwable) {
                    dialog.closeDialog()
                    Log.e("response.body()","response.body() ${t.printStackTrace()}")
                }

            }, user_id =user_id!! )
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
            if(uri==null)
                return
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