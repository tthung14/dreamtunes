package com.tuhoc.dreamtunes.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.tuhoc.dreamtunes.R
import com.tuhoc.dreamtunes.adapter.SongAdapter
import com.tuhoc.dreamtunes.bases.BaseFragment
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.databinding.FragmentProfileBinding
import com.tuhoc.dreamtunes.manager.LoginManager
import com.tuhoc.dreamtunes.ui.edit_profile.EditProfileActivity
import com.tuhoc.dreamtunes.ui.home.HomeFragment
import com.tuhoc.dreamtunes.ui.home.HomeViewModel
import com.tuhoc.dreamtunes.ui.login.LoginActivity
import com.tuhoc.dreamtunes.ui.play.SharedViewModel
import com.tuhoc.dreamtunes.utils.Constants

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var songAdapter: SongAdapter
    private lateinit var sharedViewModel: SharedViewModel

    override fun observerData() {
        super.observerData()
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        observeUser()
        observeHistorySong()

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        sharedViewModel.messageLiveData.observe(viewLifecycleOwner, Observer { songId ->
            HomeFragment.currentSongId = songId
            songAdapter.updateSong()
        })
    }

    override fun initData() {
        super.initData()
        songAdapter = SongAdapter()
        prepareRecyclerView()
    }

    override fun initView() {
        super.initView()
        userView()
    }

    override fun handleEvent() {
        super.handleEvent()
        onClick()
        onSongClick()
    }

    override fun onResume() {
        super.onResume()
        observeUser()
    }

    private fun onSongClick() {
        val user = LoginManager.getCurrentUser(requireContext())

        songAdapter.onItemClicked(object : SongAdapter.OnItemClick {
            override fun onClickListener(song: Song, p: Int, isNow: Boolean) {
                val bundle = Bundle().apply {
                    putParcelable(Constants.SONG, song)
                    putBoolean("play", isNow)
                    putInt("po", p)
                    putParcelableArrayList(Constants.LIST, ArrayList(songAdapter.getSongList()))

                }
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_profileFragment_to_playFragment,
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

    private fun prepareRecyclerView() {
        binding.apply {
            rcvHistory.adapter = songAdapter
        }
    }

    private fun userView() {
        profileViewModel.user.observe(requireActivity()) { user ->
            user?.let {
                if (user.image != null) {
                    Glide.with(requireContext())
                        .load(user.image)
                        .into(binding.imgSelf)
                }
                binding.tvUserName.text = user.userName
            }
        }
    }

    private fun observeUser() {
        val user = LoginManager.getCurrentUser(requireContext())
        user?.userId?.let { profileViewModel.getUserById(it) }
    }

    private fun observeHistorySong() {
        val user = LoginManager.getCurrentUser(requireContext())
        user?.userId?.let { profileViewModel.getHistoryListen(it) }
        profileViewModel.songs.observe(viewLifecycleOwner) { songList ->
            songAdapter.setSongList(songList.toMutableList())
        }
    }

    private fun onClick() {
        binding.btnLogout.setOnClickListener {
            // set trạng thái đăng nhập
            LoginManager.setLoggedIn(requireContext(), false, null)

            binding.progressBar.visibility = View.VISIBLE
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            activity?.finish()
        }

        binding.imgSelf.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
    }
}