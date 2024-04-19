package com.tuhoc.dreamtunes.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.databinding.SongCardBinding
import com.tuhoc.dreamtunes.ui.home.HomeFragment

class SongAdapter() : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    private var songList = mutableListOf<Song>()
    private lateinit var onItemClick: OnItemClick
    private var isNow = false

    fun getSongList(): List<Song> {
        return songList
    }

    fun onItemClicked(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    fun setSongList(songList: List<Song>) {
        this.songList = songList.toMutableList()
        notifyDataSetChanged()
    }

    fun updateSong() {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = SongCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val currentItem = songList[position]
        holder.binding.apply {
            Glide.with(holder.itemView)
                .load(currentItem.image)
                .into(imgSong)

            tvSongName.text = currentItem.songName
            tvArtist.text = currentItem.singer?.singerName
        }

        if (currentItem.songId == HomeFragment.currentSongId) {

            holder.binding.tvSongName.setTextColor(Color.parseColor("#42C83C"))
        } else {

            holder.binding.tvSongName.setTextColor(Color.parseColor("#FFFFFF"))
        }

        holder.itemView.setOnClickListener {
            isNow = currentItem.songId == HomeFragment.currentSongId
            onItemClick.onClickListener(currentItem, holder.adapterPosition, isNow)
        }
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    class SongViewHolder(val binding: SongCardBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun onClickListener(song: Song, position: Int, isNow: Boolean)
    }
}