package com.shambavi.thericecompany

interface CartListener {
    fun addCart(pid:String,attributeId:String)
    fun deleteCart(cid:String)
}