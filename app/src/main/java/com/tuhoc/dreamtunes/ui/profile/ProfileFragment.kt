package com.tuhoc.dreamtunes.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tuhoc.dreamtunes.adapter.SingerAdapter
import com.tuhoc.dreamtunes.adapter.SliderAdapter
import com.tuhoc.dreamtunes.adapter.SongAdapter
import com.tuhoc.dreamtunes.adapter.TypeAdapter
import com.tuhoc.dreamtunes.bases.BaseFragment
import com.tuhoc.dreamtunes.databinding.FragmentHomeBinding
import com.tuhoc.dreamtunes.databinding.FragmentProfileBinding
import com.tuhoc.dreamtunes.manager.LoginManager
import com.tuhoc.dreamtunes.manager.LoginManager.getCurrentUser
import com.tuhoc.dreamtunes.ui.edit_profile.EditProfileActivity
import com.tuhoc.dreamtunes.ui.home.HomeViewModel
import com.tuhoc.dreamtunes.ui.login.LoginActivity

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private lateinit var profileViewModel: ProfileViewModel

    override fun observerData() {
        super.observerData()
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        observeUser()
    }

    override fun initView() {
        super.initView()
        userView()
    }

    override fun handleEvent() {
        super.handleEvent()
        onClick()
    }

    override fun onResume() {
        super.onResume()
        observeUser()
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
        val user = getCurrentUser(requireContext())
        user?.userId?.let { profileViewModel.getUserById(it) }
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