package com.tuhoc.dreamtunes.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuhoc.dreamtunes.data.pojo.Singer
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.databinding.SongCardBinding
import com.tuhoc.dreamtunes.ui.home.HomeFragment

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    private var itemList = mutableListOf<Any>()
    private lateinit var onItemClick: OnItemClick
    private var isNow = false

    fun getSongList(): List<Any> {
        return itemList
    }

    fun onItemClicked(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    fun setItemList(itemList: List<Any>) {
        this.itemList = itemList.toMutableList()
        notifyDataSetChanged()
    }

    fun updateSong() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = SongCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchAdapter.SearchViewHolder, position: Int) {
        val currentItem = itemList[position]

        // Kiểm tra nếu currentItem là một Song
        if (currentItem is Song) {
            holder.binding.apply {
                Glide.with(holder.itemView)
                    .load(currentItem.image)
                    .into(imgSong)

                tvSongName.text = currentItem.songName
                tvArtist.text = currentItem.singer?.singerName

                root.setOnClickListener {
                    isNow = currentItem.songId == HomeFragment.currentSongId
                    onItemClick.onSongClickListener(currentItem, holder.adapterPosition, isNow)
                }
            }

            if (currentItem.songId == HomeFragment.currentSongId) {

                holder.binding.tvSongName.setTextColor(Color.parseColor("#42C83C"))
            } else {

                holder.binding.tvSongName.setTextColor(Color.parseColor("#FFFFFF"))
            }
        }

        // Kiểm tra nếu currentItem là một Singer
        else if (currentItem is Singer) {
            holder.binding.apply {
                Glide.with(holder.itemView)
                    .load(currentItem.image)
                    .into(imgSong)

                tvSongName.text = currentItem.singerName
                tvArtist.text = ""
                // Bắt sự kiện click cho Singer
                root.setOnClickListener {
                    onItemClick.onSingerClickListener(currentItem)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class SearchViewHolder(val binding: SongCardBinding) : RecyclerView.ViewHolder(binding.root)

    fun filterList(filterlist: ArrayList<Any>) {
        itemList = filterlist
        notifyDataSetChanged()
    }

    interface OnItemClick {
        fun onSongClickListener(song: Song, position: Int, isNow: Boolean)
        fun onSingerClickListener(singer: Singer)
    }
}