package com.shambavi.thericecompany.categories

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gadiwalaUser.Models.Product
import com.gadiwalaUser.services.DataManager.Companion.ROOT_URL
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.LayoutProductProductItemBinding
import com.shambavi.thericecompany.listeners.ProductListener
import com.shambavi.thericecompany.products.ProductDetailsActivity
import com.shambavi.thericecompany.utils.Utils

class AllProductsAdapter: RecyclerView.Adapter<AllProductsAdapter.ProductViewHolder>() {

    var productList:ArrayList<Product> = arrayListOf()
    lateinit var productListner: ProductListener
    class ProductViewHolder(val binding: LayoutProductProductItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val binding=LayoutProductProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       // val view=LayoutInflater.from(parent.context).inflate(R.layout.layout_home_product_item,parent,false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        var obj=productList.get(position)
        holder.binding.txtMrp.paintFlags = holder.binding.txtMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        var displayPrice=""
        if(obj.attributes.size>0) {
            var attribution = obj.attributes.get(0)
            displayPrice="${attribution.weight} ${Utils.RUPEE_SYMBOL}${attribution.prices}"
        }
        holder.binding.txtProductName.text="${obj.categoryIdName}"
        holder.binding.txtMrp.text="${Utils.RUPEE_SYMBOL}${obj.mrpPrice}"
        holder.binding.txtOff.text="${obj.price_off}%"
        holder.binding.txtOurPrice.text="${Utils.RUPEE_SYMBOL}${obj.ourPrice}"
        if(obj.marketPrice!=null&& obj.marketPrice!!.isNotEmpty())
        {
            if(!obj.marketPrice!!.equals("0"))
            {
                holder.binding.txtMarketPrice.text="${Utils.RUPEE_SYMBOL}${obj.marketPrice}"
            }else
            {
                holder.binding.lnrMarketPrice.visibility=View.GONE
            }

        }else
        {
            holder.binding.lnrMarketPrice.visibility=View.GONE
        }

        holder.binding.txtProductCategory.text="${obj.title}"
        holder.binding.tvQuantity.setText("${obj.quantity}")
        if(obj.user_rating.isEmpty()||obj.user_rating.trim().equals("0"))
            holder.binding.lnrRating.visibility=View.INVISIBLE
        else {
            holder.binding.lnrRating.visibility=View.VISIBLE

            holder.binding.txtRating.setText("${obj.user_rating}")
        }
        Glide.with(holder.binding.img.context).load(ROOT_URL+obj.image).placeholder(R.drawable.item1).into(holder.binding.img)

        holder.binding.txtProductName.setOnClickListener {
            val ctx=holder.binding.txtProductName.context
            val intent=Intent(ctx,ProductDetailsActivity::class.java)
            intent.putExtra("product_id",productList.get(position).id)

            ctx.startActivity(intent)
       }
        if(obj.cartId.isEmpty())
        {
            holder.binding.lnrAdd.visibility=View.VISIBLE
            holder.binding.quantityLayout.visibility=View.GONE
        }else
        {
            holder.binding.lnrAdd.visibility=View.GONE
            holder.binding.quantityLayout.visibility=View.VISIBLE

        }
        holder.binding.lnrAdd.setOnClickListener {
            productListner.addProduct(productList.get(position).id.toString(),productList.get(position).attributeId.toString())
        }
        holder.binding.btnPlus.setOnClickListener {
            var qnty=Integer.parseInt(productList.get(position).quantity)
            qnty=qnty+1
            holder.binding.tvQuantity.setText("${qnty}")
            productListner. updateProduct(productList.get(position).cartId.toString(),qnty)
        }
        holder.binding.btnMinus.setOnClickListener {
            var qnty=Integer.parseInt(productList.get(position).quantity)
            qnty=qnty-1

            if(qnty==0) {
                productListner.deleteProduct(productList.get(position).cartId.toString())
            }
                else {
                holder.binding.tvQuantity.setText("${qnty}")
                productListner.updateProduct(productList.get(position).cartId.toString(), qnty)
            }
        }
        holder.binding.root.setOnClickListener {
            productList.get(position).id?.let { it1 -> productListner.onProductClick(it1) }


        }
    }

    fun setListener(lister: ProductListener) {
            productListner=lister
    }

    fun setListData(products: ArrayList<Product>)
    {
        productList.clear()
        productList.addAll(products)
        notifyDataSetChanged()
    }


}