package com.shambavi.thericecompany.products

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.ActivityProductDetailsBinding

class ProductDetailsActivity : AppCompatActivity() {
    lateinit var binding:ActivityProductDetailsBinding
    var  chipList:List<ChipPrices>? =null
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
         chipList = listOf(ChipPrices("1kg","1",false),
            ChipPrices("2kg","2",false),
            ChipPrices("3kg","3",false),
            ChipPrices("4kg","4",false),
            ChipPrices("5kg","5",false),
            ChipPrices("6kg","6",false),
            ChipPrices("6kg","7",false),
            ChipPrices("6kg","8",false),
            ChipPrices("6kg","9",false),
            ChipPrices("6kg","10",false),
            ChipPrices("6kg","11",false),
            )

            setData()
            val imageList = ArrayList<SlideModel>() // Create image list

            imageList.add(SlideModel( R.drawable.item1, ScaleTypes.CENTER_CROP) )// for one image
            imageList.add(SlideModel( R.drawable.item2, ScaleTypes.CENTER_CROP) )// for one image
          binding. imageSlider.setImageList(imageList)


            binding.btnAddToCart.setOnClickListener {
                finish()
            }
            binding.backButton.setOnClickListener {
                finish()
            }

        }

    fun setData()
    {
    val lp= FlexboxLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        FlexboxLayout.LayoutParams.WRAP_CONTENT
    )
        lp.marginStart=10
        lp.marginEnd=10
        binding.flexboxLayout.removeAllViews()
        for (chipText in chipList!!) {
            val chip = Chip(this)
            chip.text = chipText.price
            chip.id=Integer.parseInt(chipText.id)
            chip.isChecked=chipText.isSelected
           // chip.layoutParams=lp

            if(!chipText.isSelected) {
                chip.chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )


                )
                chip.setTextColor(ColorStateList.valueOf(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.black
                    )
                )


                )

            }
            else {
                chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.green))
                chip.setTextColor(ColorStateList.valueOf(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                ))
            }

            chip.setOnClickListener {
                updateChips(it.id)
            }

            binding.flexboxLayout.addView(chip)
        }
    }

    private fun updateChips(id: Int) {
        chipList!!.forEach {
            if(it.id==id.toString())
            {
                it.isSelected=true
            }else
            {
                it.isSelected=false
            }
        }
        setData()

    }

    data class ChipPrices(val price:String, val id:String, var isSelected:Boolean=false)
}