package com.tuhoc.dreamtunes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import com.tuhoc.dreamtunes.databinding.SongSliderBinding

class SliderAdapter: SliderViewAdapter<SliderAdapter.SliderViewHolder>() {
    private var sliderList = mutableListOf<String>()

    fun setSliderList(sliderList: List<String>) {
        this.sliderList = sliderList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapter.SliderViewHolder {
        val binding = SongSliderBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapter.SliderViewHolder?, position: Int) {
        val currentItem = sliderList[position]
        viewHolder?.binding?.apply {
            Glide.with(viewHolder.itemView).load(currentItem).fitCenter()
                .into(imgPhoto)
        }
    }

    override fun getCount(): Int {
        return sliderList.size
    }

    class SliderViewHolder(val binding: SongSliderBinding) : SliderViewAdapter.ViewHolder(binding.root)
}