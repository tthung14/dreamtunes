package com.tuhoc.dreamtunes.ui.playlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.tuhoc.dreamtunes.R
import com.tuhoc.dreamtunes.adapter.PlaylistSongDeleteAdapter
import com.tuhoc.dreamtunes.adapter.SongAdapter
import com.tuhoc.dreamtunes.bases.BaseFragment
import com.tuhoc.dreamtunes.data.pojo.Album
import com.tuhoc.dreamtunes.data.pojo.Playlist
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.data.pojo.Type
import com.tuhoc.dreamtunes.databinding.FragmentPlaylistBinding
import com.tuhoc.dreamtunes.databinding.FragmentSongTypeBinding
import com.tuhoc.dreamtunes.manager.LoginManager
import com.tuhoc.dreamtunes.ui.home.HomeFragment
import com.tuhoc.dreamtunes.ui.home.HomeViewModel
import com.tuhoc.dreamtunes.ui.play.SharedViewModel
import com.tuhoc.dreamtunes.ui.songtype.SongTypeViewModel
import com.tuhoc.dreamtunes.utils.Constants
import com.tuhoc.dreamtunes.utils.dialog.DeletePlaylistDialog
import com.tuhoc.dreamtunes.utils.dialog.EditPlaylistDialog
import com.tuhoc.dreamtunes.utils.dialog.PlaylistDialog
import kotlinx.coroutines.launch
import kotlin.math.log

class PlaylistFragment : BaseFragment<FragmentPlaylistBinding>(FragmentPlaylistBinding::inflate) {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var playlistViewModel: PlaylistViewModel
    private lateinit var playlistSongDeleteAdapter: PlaylistSongDeleteAdapter
    private lateinit var sharedViewModel: SharedViewModel

    override fun observerData() {
        super.observerData()
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        playlistViewModel = ViewModelProvider(this)[PlaylistViewModel::class.java]

        observeSongs()

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        sharedViewModel.messageLiveData.observe(viewLifecycleOwner, Observer { songId ->
            HomeFragment.currentSongId = songId
            playlistSongDeleteAdapter.updateSong()
        })
    }

    override fun initData() {
        super.initData()
        playlistSongDeleteAdapter = PlaylistSongDeleteAdapter()
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
            rcvSong.adapter = playlistSongDeleteAdapter
        }
    }

    private fun observeSongs() {
        val playlist = arguments?.getParcelable<Playlist>(Constants.PLAYLIST)
        if (playlist != null) {
            binding.tvPlaylistName.text = playlist.playlistName
            playlist.playlistId?.let { playlistViewModel.getSongsByPlaylist(it) }
            playlistViewModel.songs.observe(requireActivity()) { songList ->
                playlistSongDeleteAdapter.setSongList(songList.toMutableList())
            }
        }
    }

    private fun onSongClick() {
        val playlist = arguments?.getParcelable<Playlist>(Constants.PLAYLIST)
        val user = LoginManager.getCurrentUser(requireContext())

        playlistSongDeleteAdapter.onItemClicked(object : PlaylistSongDeleteAdapter.OnItemClick {
            override fun onClickListener(song: Song, p: Int, isNow: Boolean) {
                // Chuyển đến màn hình play music khi được click
                val bundle = Bundle().apply {
                    putParcelable(Constants.SONG, song)
                    putBoolean("play", isNow)
                    putInt("po", p)
                    putParcelableArrayList(Constants.LIST, ArrayList(playlistSongDeleteAdapter.getSongList()))
                }
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_playlistFragment_to_playFragment,
                    bundle
                )

                user!!.userId?.let { song.songId?.let { it1 ->
                    homeViewModel.latestListenTime(it,
                        it1
                    )
                } }
            }

            override fun onClickEvent(song: Song) {
                playlist!!.playlistId?.let {
                    song.songId?.let { it1 ->
                        playlistViewModel.deleteSongByPlaylist(it, it1) { check, message ->
                            if (check) {
                                observeSongs()
                            }
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }

    private fun onClick() {
        val playlist = arguments?.getParcelable<Playlist>(Constants.PLAYLIST)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.imgDelete.setOnClickListener {
            val dialog = DeletePlaylistDialog()
            dialog.show(childFragmentManager, "DeletePlaylistDialog")
        }

        binding.btnEditPlaylistName.setOnClickListener {
            val dialog = EditPlaylistDialog()
            dialog.show(childFragmentManager, "EditPlaylistDialog")
        }

        binding.btnAddPlaylist.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable(Constants.PLAYLIST, playlist)
            }
            Navigation.findNavController(requireView()).navigate(
                R.id.action_playlistFragment_to_listSongFragment,
                bundle
            )
        }
    }

    fun deletePlaylist() {
        val playlist = arguments?.getParcelable<Playlist>(Constants.PLAYLIST)
        playlist!!.playlistId?.let {
            playlistViewModel.deletePlaylist(it)
        }
        Navigation.findNavController(requireView()).popBackStack()
    }

    fun updatePlaylist(playlistName: String) {
        val playlist = arguments?.getParcelable<Playlist>(Constants.PLAYLIST)
        playlist!!.playlistId?.let {
            playlistViewModel.updatePlaylist(it, playlistName) { check, message ->
                if (check) {
                    binding.tvPlaylistName.text = playlistName
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}