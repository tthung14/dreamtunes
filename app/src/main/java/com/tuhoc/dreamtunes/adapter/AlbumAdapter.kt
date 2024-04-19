package com.tuhoc.dreamtunes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuhoc.dreamtunes.data.pojo.Album
import com.tuhoc.dreamtunes.databinding.TypeCardBinding

class AlbumAdapter: RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {
    private var albumList = mutableListOf<Album>()

    private lateinit var onItemClick: OnItemClick

    fun onItemClicked(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    fun setAlbumList(albumList: List<Album>) {
        this.albumList = albumList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = TypeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumAdapter.AlbumViewHolder, position: Int) {
        val currentItem = albumList[position]
        holder.binding.apply {
            Glide.with(holder.itemView)
                .load(currentItem.image)
                .into(imgType)

            tvType.text = currentItem.albumName
        }

        holder.itemView.setOnClickListener {
            onItemClick.onClickListener(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    class AlbumViewHolder(val binding: TypeCardBinding): RecyclerView.ViewHolder(binding.root)

    interface OnItemClick{
        fun onClickListener(album: Album)
    }
}