package com.shambavi.thericecompany.listeners

interface ProductListener {
    fun addProduct(product_id:String,attribution_id:String)
    fun deleteProduct(cart_id:String)
    fun updateProduct(product_id:String,qnty:Int)
    fun onProductClick(product_id:String)
}