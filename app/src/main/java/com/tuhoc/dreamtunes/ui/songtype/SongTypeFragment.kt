package com.tuhoc.dreamtunes.ui.songtype

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tuhoc.dreamtunes.R
import com.tuhoc.dreamtunes.adapter.SingerAdapter
import com.tuhoc.dreamtunes.adapter.SliderAdapter
import com.tuhoc.dreamtunes.adapter.SongAdapter
import com.tuhoc.dreamtunes.adapter.TypeAdapter
import com.tuhoc.dreamtunes.bases.BaseFragment
import com.tuhoc.dreamtunes.data.pojo.Album
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.data.pojo.Type
import com.tuhoc.dreamtunes.databinding.FragmentHomeBinding
import com.tuhoc.dreamtunes.databinding.FragmentSongTypeBinding
import com.tuhoc.dreamtunes.manager.LoginManager
import com.tuhoc.dreamtunes.ui.home.HomeFragment
import com.tuhoc.dreamtunes.ui.home.HomeViewModel
import com.tuhoc.dreamtunes.ui.play.SharedViewModel
import com.tuhoc.dreamtunes.utils.Constants
import com.tuhoc.dreamtunes.utils.Constants.ALBUM
import com.tuhoc.dreamtunes.utils.Constants.SONG
import com.tuhoc.dreamtunes.utils.Constants.TYPE

class SongTypeFragment : BaseFragment<FragmentSongTypeBinding>(FragmentSongTypeBinding::inflate) {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var songTypeViewModel: SongTypeViewModel
    private lateinit var songAdapter: SongAdapter
    private lateinit var sharedViewModel: SharedViewModel

    override fun observerData() {
        super.observerData()
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        songTypeViewModel = ViewModelProvider(this)[SongTypeViewModel::class.java]
        observeSongs()

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        sharedViewModel.messageLiveData.observe(viewLifecycleOwner, Observer { songId ->
            HomeFragment.currentSongId = songId
            songAdapter.updateSong()
        })
    }

    override fun initData() {
        super.initData()
        songAdapter = SongAdapter()
    }

    override fun initView() {
        super.initView()
        prepareRecyclerView()
    }

    override fun handleEvent() {
        super.handleEvent()
        onSongClick()
        onClick()
    }

    private fun prepareRecyclerView() {
        binding.apply {
            rcvSong.adapter = songAdapter
        }
    }

    private fun observeSongs() {
        val type = arguments?.getParcelable<Type>(TYPE)
        if (type != null) {
            binding.tvTypeName.text = type.typeName
            type.typeId?.let { songTypeViewModel.getSongsByType(it) }
            songTypeViewModel.songs.observe(requireActivity()) { songList ->
                songAdapter.setSongList(songList.toMutableList())
            }
        }

        val album = arguments?.getParcelable<Album>(ALBUM)
        if (album != null) {
            binding.tvTypeName.text = "Album " + album.albumName
            album.albumId?.let { songTypeViewModel.getSongsByAlbum(it) }
            songTypeViewModel.songs.observe(requireActivity()) { songList ->
                songAdapter.setSongList(songList.toMutableList())
            }
        }
    }

    private fun onSongClick() {
        val user = LoginManager.getCurrentUser(requireContext())

        songAdapter.onItemClicked(object : SongAdapter.OnItemClick {
            override fun onClickListener(song: Song,p:Int, isNow: Boolean) {
                val bundle = Bundle().apply {
                    putParcelable(SONG, song)
                    putBoolean("play", isNow)
                    putInt("po", p)
                    putParcelableArrayList(Constants.LIST, ArrayList(songAdapter.getSongList()))

                }
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_songTypeFragment_to_playFragment,
                    bundle
                )

                user!!.userId?.let { song.songId?.let { it1 ->
                    homeViewModel.latestListenTime(it,
                        it1
                    )
                } }
            }
        })
    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}