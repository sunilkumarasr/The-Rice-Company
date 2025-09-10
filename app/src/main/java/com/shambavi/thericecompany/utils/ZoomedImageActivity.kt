package com.shambavi.thericecompany.utils

import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.shambavi.thericecompany.databinding.ActivityZoomedImageBinding

class ZoomedImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityZoomedImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityZoomedImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Make the dialog window fill the screen
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        // Optional: If you also want to remove any default dialog padding/insets that might be applied by the theme
        // window?.setBackgroundDrawableResource(android.R.color.transparent) // Already handled by theme and layout background

        val imageUrl = intent.getStringExtra("image_url")

        if (imageUrl != null) {
            Glide.with(this)
                .load(imageUrl)
                .into(binding.zoomedImageView)
        }

        binding.closeButton.setOnClickListener {
            finish() // Close the activity
        }
    }
}
