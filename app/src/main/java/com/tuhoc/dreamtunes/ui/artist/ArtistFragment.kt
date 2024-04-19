package com.tuhoc.dreamtunes.ui.artist

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tuhoc.dreamtunes.R
import com.tuhoc.dreamtunes.adapter.AlbumAdapter
import com.tuhoc.dreamtunes.adapter.SongAdapter
import com.tuhoc.dreamtunes.adapter.TypeAdapter
import com.tuhoc.dreamtunes.bases.BaseFragment
import com.tuhoc.dreamtunes.data.pojo.Album
import com.tuhoc.dreamtunes.data.pojo.Singer
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.data.pojo.Type
import com.tuhoc.dreamtunes.databinding.FragmentArtistBinding
import com.tuhoc.dreamtunes.ui.home.HomeFragment
import com.tuhoc.dreamtunes.ui.play.SharedViewModel
import com.tuhoc.dreamtunes.utils.Constants
import com.tuhoc.dreamtunes.utils.Constants.SINGER

class ArtistFragment : BaseFragment<FragmentArtistBinding>(FragmentArtistBinding::inflate) {

    private lateinit var singerViewModel: SingerViewModel
    private lateinit var songAdapter: SongAdapter
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var sharedViewModel: SharedViewModel
    override fun initData() {
        super.initData()
        songAdapter = SongAdapter()
        albumAdapter = AlbumAdapter()
    }

    override fun initView() {
        super.initView()
        prepareRecyclerView()
    }

    override fun observerData() {
        super.observerData()
        singerViewModel = ViewModelProvider(this)[SingerViewModel::class.java]
        observeSinger()

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        sharedViewModel.messageLiveData.observe(viewLifecycleOwner, Observer { songId ->
            HomeFragment.currentSongId = songId
            songAdapter.updateSong()
        })

    }

    override fun handleEvent() {
        super.handleEvent()
        onSongClick()
        onAlbumClick()
        onClick()
    }

    private fun prepareRecyclerView() {
        binding.apply {
            rcvSong.adapter = songAdapter
            rcvAlbum.adapter = albumAdapter
        }
    }

    private fun observeSinger() {
        val singer = arguments?.getParcelable<Singer>(SINGER)

        binding.apply {
            Glide.with(requireActivity())
                .load(singer?.image)
                .into(imgSinger)
            tvSingerName.text = singer?.singerName
        }

        singer?.singerId?.let { singerViewModel.getAlbumsBySinger(it) }
        singerViewModel.albums.observe(requireActivity()) { albumList ->
            binding.tvAlbums.text = albumList.size.toString()
            albumAdapter.setAlbumList(albumList.toMutableList())
        }

//        singer?.let { singerViewModel.getSongsBySinger(it.singerId) }
        singer?.singerId?.let { singerViewModel.getSongsBySinger(it) }
        singerViewModel.songs.observe(requireActivity()) { songList ->
            songAdapter.setSongList(songList.toMutableList())
        }
    }

    private fun onSongClick() {
        songAdapter.onItemClicked(object : SongAdapter.OnItemClick {
            override fun onClickListener(song: Song,p:Int, isNow: Boolean) {
                // Chuyển đến màn hình play music khi được click
                val bundle = Bundle().apply {
                    putParcelable(Constants.SONG, song)
                    putBoolean("play", isNow)
                    putInt("po", p)
                    putParcelableArrayList(Constants.LIST, ArrayList(songAdapter.getSongList()))

                }
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_artistFragment_to_playFragment,
                    bundle
                )
            }
        })
    }

    private fun onAlbumClick() {
        albumAdapter.onItemClicked(object : AlbumAdapter.OnItemClick {
            override fun onClickListener(album: Album) {
                val bundle = Bundle().apply {
                    putParcelable(Constants.ALBUM, album)
                }
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_artistFragment_to_songTypeFragment,
                    bundle
                )
            }
        })
    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}