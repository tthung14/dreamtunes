package com.tuhoc.dreamtunes.bases

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T, VB : ViewBinding> : RecyclerView.Adapter<BaseAdapter<T, VB>.BaseViewHolder>() {

    val itemList: MutableList<T> = mutableListOf()

    abstract inner class BaseViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root) {
        abstract fun setData(item: T)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        itemList.getOrNull(position)?.let { data ->
            holder.setData(data)
        }
    }

    fun setItemList(itemList: MutableList<T>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

}