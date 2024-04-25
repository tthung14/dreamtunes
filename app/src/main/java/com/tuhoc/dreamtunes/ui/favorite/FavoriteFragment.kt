package com.tuhoc.dreamtunes.ui.favorite

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.tuhoc.dreamtunes.R
import com.tuhoc.dreamtunes.adapter.PlaylistAdapter
import com.tuhoc.dreamtunes.adapter.SongAdapter
import com.tuhoc.dreamtunes.bases.BaseFragment
import com.tuhoc.dreamtunes.data.pojo.Playlist
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.databinding.FragmentFavoriteBinding
import com.tuhoc.dreamtunes.manager.LoginManager
import com.tuhoc.dreamtunes.ui.home.HomeFragment
import com.tuhoc.dreamtunes.ui.play.SharedViewModel
import com.tuhoc.dreamtunes.utils.Constants
import com.tuhoc.dreamtunes.utils.dialog.PlaylistDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var songAdapter: SongAdapter
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun observerData() {
        super.observerData()
        favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        observeFavorite()
        observePlaylist()

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        sharedViewModel.messageLiveData.observe(viewLifecycleOwner, Observer { songId ->
            HomeFragment.currentSongId = songId
            songAdapter.updateSong()
        })
    }

    override fun initData() {
        super.initData()
        songAdapter = SongAdapter()
        playlistAdapter = PlaylistAdapter()
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
            rcvFavorite.adapter = songAdapter
            rcvPlaylist.adapter = playlistAdapter
            rcvPlaylist.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun onSongClick() {
        songAdapter.onItemClicked(object : SongAdapter.OnItemClick {
            override fun onClickListener(song: Song, p:Int,isNow: Boolean) {
                // Chuyển đến màn hình play music khi được click
                val bundle = Bundle().apply {
                    putParcelable(Constants.SONG, song)
                    putBoolean("play", isNow)
                    putInt("po", p)
                    putParcelableArrayList(Constants.LIST, ArrayList(songAdapter.getSongList()))
                }
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_favoriteFragment_to_playFragment,
                    bundle
                )
            }
        })
    }

    private fun onClick() {
        binding.btnAdd.setOnClickListener{
            val dialog = PlaylistDialog()
            dialog.show(childFragmentManager, "PlaylistDialog")
        }

        playlistAdapter.onItemClicked(object : PlaylistAdapter.OnItemClick {
            override fun onClickListener(playlist: Playlist) {
                val bundle = Bundle().apply {
                    putParcelable(Constants.PLAYLIST, playlist)
                }
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_favoriteFragment_to_playlistFragment,
                    bundle
                )
            }
        })
    }

    private fun observeFavorite() {
        val user = LoginManager.getCurrentUser(requireContext())
        user?.userId?.let { favoriteViewModel.getFavoriteSongs(it) }
        favoriteViewModel.songs.observe(viewLifecycleOwner) { songList ->
            Log.d("TAG", "observeFavorite: " + songList.size)
            songAdapter.setSongList(songList.toMutableList())
        }
    }

    private fun observePlaylist() {
        val user = LoginManager.getCurrentUser(requireContext())
        user?.userId?.let { favoriteViewModel.getPlaylistsByUser(it) }
        favoriteViewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            playlistAdapter.setPlaylistList(playlists.toMutableList())
        }
    }

    fun createPlaylist(playlistName: String) {
        val user = LoginManager.getCurrentUser(requireContext())
        user?.let {
            favoriteViewModel.addPlaylist(playlistName, Constants.date(), it) { success ->
                if (success) {
                    observePlaylist()
                }
            }
        }
    }
}