package com.tuhoc.dreamtunes.adapter;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuhoc.dreamtunes.data.pojo.Singer;
import com.tuhoc.dreamtunes.databinding.SingerCardBinding

public class SingerAdapter : RecyclerView.Adapter<SingerAdapter.SingerViewHolder>() {
    private var singerList = mutableListOf<Singer>()

    private lateinit var onItemClick: OnItemClick

    fun onItemClicked(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    fun setSingerList(singerList: List<Singer>) {
        this.singerList = singerList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingerViewHolder {
        val binding = SingerCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SingerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SingerAdapter.SingerViewHolder, position: Int) {
        val currentItem = singerList[position]
        holder.binding.apply {
            Glide.with(holder.itemView)
                .load(currentItem.image)
                .into(imgSinger)

            tvType.text = currentItem.singerName
        }

        holder.itemView.setOnClickListener {
            onItemClick.onClickListener(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return singerList.size
    }

    class SingerViewHolder(val binding: SingerCardBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun onClickListener(singer: Singer)
    }
}