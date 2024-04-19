package com.tuhoc.dreamtunes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuhoc.dreamtunes.data.pojo.Album
import com.tuhoc.dreamtunes.data.pojo.Playlist
import com.tuhoc.dreamtunes.databinding.PlaylistCardBinding
import com.tuhoc.dreamtunes.databinding.TypeCardBinding

class PlaylistAdapter: RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {
    private var playlistList = mutableListOf<Playlist>()

    private lateinit var onItemClick: OnItemClick

    fun onItemClicked(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    fun setPlaylistList(playlistList: List<Playlist>) {
        this.playlistList = playlistList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = PlaylistCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistAdapter.PlaylistViewHolder, position: Int) {
        val currentItem = playlistList[position]
        holder.binding.tvPlaylist.text = currentItem.playlistName

        holder.itemView.setOnClickListener {
            onItemClick.onClickListener(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return playlistList.size
    }

    class PlaylistViewHolder(val binding: PlaylistCardBinding): RecyclerView.ViewHolder(binding.root)

    interface OnItemClick{
        fun onClickListener(playlist: Playlist)
    }
}