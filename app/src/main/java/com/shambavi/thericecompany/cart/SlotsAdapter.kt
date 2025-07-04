package com.shambavi.thericecompany.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gadiwalaUser.Models.CartModel
import com.gadiwalaUser.Models.Slots
import com.gadiwalaUser.services.DataManager.Companion.ROOT_URL
import com.shambavi.thericecompany.R
import com.shambavi.thericecompany.databinding.LayoutCartItemBinding
import com.shambavi.thericecompany.databinding.LayoutSlotItemBinding
import com.shambavi.thericecompany.listeners.ProductListener
import com.shambavi.thericecompany.utils.Utils.Companion.RUPEE_SYMBOL

class SlotsAdapter: RecyclerView.Adapter<SlotsAdapter.CartViewHolder>() {

    var slotList:ArrayList<Slots> = arrayListOf()
    var keysList:ArrayList<String> = arrayListOf()
    lateinit var productListener: ProductListener
    var hashMap=HashMap<String,ArrayList<Slots>>()
    fun setListener(productListener: ProductListener)
    {
       this .productListener=productListener
    }


    fun setSlotLists(slotList:ArrayList<Slots> )
    {
        hashMap.clear()
        slotList.forEach {

            if(hashMap.containsKey(it.date))
            {
                var list=hashMap.get(it.date) as ArrayList<Slots>
                list.add(it)
                hashMap.put(it.date!!,list)
            }else
            {
                keysList.add(it.date!!)
                var list=ArrayList<Slots>()
                list.add(it)
                hashMap.put(it.date!!,list)

            }
        }
       this .slotList=slotList
        notifyDataSetChanged()
    }

    class CartViewHolder(val binding: LayoutSlotItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.layout_home_top_categories,parent,false)
        val binding= LayoutSlotItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return keysList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        var slot=slotList.get(position)
        holder.binding.tvSlot.setText("${slot.date}")
        holder.binding.tvSlot.visibility=View.GONE
        holder.binding.tvDate.text="${slot.date}"
        holder.binding.tvSlot.text="${slot.startTime} - ${slot.endTime}"
        var slotsAdapterInner=SlotsAdapterInner()
        slotsAdapterInner.setListener(productListener)
        slotsAdapterInner.setSlotLists(hashMap.get(keysList.get(position))!! as ArrayList<Slots> )
        holder.binding.recyclerSlots.adapter=slotsAdapterInner
        if(position>0)
        {
            if(slot.date==slotList.get(position-1).date)
                holder.binding.tvDate.visibility= View.GONE
            else holder.binding.tvDate.visibility= View.VISIBLE
        }else
        {
            holder.binding.tvDate.visibility= View.VISIBLE
        }

        holder.binding.tvSlot.setOnClickListener {
            productListener.addProduct(slot.id.toString(),"${slot.date} \n${holder.binding.tvSlot.text}")
        }
    }
}