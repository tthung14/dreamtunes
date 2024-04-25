package com.tuhoc.dreamtunes.ui.home

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.smarteist.autoimageslider.SliderView
import com.tuhoc.dreamtunes.R
import com.tuhoc.dreamtunes.adapter.SingerAdapter
import com.tuhoc.dreamtunes.adapter.SliderAdapter
import com.tuhoc.dreamtunes.adapter.SongAdapter
import com.tuhoc.dreamtunes.adapter.TypeAdapter
import com.tuhoc.dreamtunes.bases.BaseFragment
import com.tuhoc.dreamtunes.data.pojo.Singer
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.data.pojo.Type
import com.tuhoc.dreamtunes.databinding.FragmentHomeBinding
import com.tuhoc.dreamtunes.manager.LoginManager
import com.tuhoc.dreamtunes.ui.play.SharedViewModel
import com.tuhoc.dreamtunes.utils.Constants.LIST
import com.tuhoc.dreamtunes.utils.Constants.SINGER
import com.tuhoc.dreamtunes.utils.Constants.SONG
import com.tuhoc.dreamtunes.utils.Constants.TYPE

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var typeAdapter: TypeAdapter
    private lateinit var singerAdapter: SingerAdapter
    private lateinit var songAdapter: SongAdapter
    private lateinit var sharedViewModel: SharedViewModel

    companion object {
        var currentSongId = -1
    }

    override fun initData() {
        super.initData()
        sliderAdapter = SliderAdapter()
        typeAdapter = TypeAdapter()
        singerAdapter = SingerAdapter()
        songAdapter = SongAdapter()
    }

    override fun initView() {
        super.initView()
        prepareRecyclerView()
    }

    override fun observerData() {
        super.observerData()
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        observeSliders()
        observeTypes()
        observeSingers()
        observeRandomSongs()

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        sharedViewModel.messageLiveData.observe(viewLifecycleOwner, Observer { songId ->
            currentSongId = songId
            songAdapter.updateSong()
        })

    }

    override fun handleEvent() {
        super.handleEvent()
        onTypeClick()
        onSingerClick()
        onSongClick()
    }

    private fun prepareRecyclerView() {
        binding.apply {
            rcvType.adapter = typeAdapter
            rcvSinger.adapter = singerAdapter
            rcvRandom.adapter = songAdapter

            // slider
            slvPhoto.setSliderAdapter(sliderAdapter)
            slvPhoto.apply {
                autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
                scrollTimeInSec = 3
                isAutoCycle = true
                startAutoCycle()
            }
        }
    }

    private fun observeSliders() {
        homeViewModel.getSliders().observe(viewLifecycleOwner) { sliders ->
            sliderAdapter.setSliderList(sliders)
        }
    }

    private fun observeTypes() {
        homeViewModel.fetchMusicTypes()
        homeViewModel.types.observe(viewLifecycleOwner, Observer {
            typeAdapter.setTypeList(it)
        })
    }

    private fun observeSingers() {
        homeViewModel.fetchMusicSingers()
        homeViewModel.singers.observe(viewLifecycleOwner, Observer {
            singerAdapter.setSingerList(it)
        })
    }

    private fun observeRandomSongs() {
        homeViewModel.fetchMusicRandomSongs()
        homeViewModel.randomSongs.observe(viewLifecycleOwner, Observer {
            songAdapter.setSongList(it)
        })
    }

    private fun onSongClick() {
        val user = LoginManager.getCurrentUser(requireContext())

        songAdapter.onItemClicked(object : SongAdapter.OnItemClick {
            override fun onClickListener(song: Song, p:Int,isNow: Boolean) {
                val bundle = Bundle().apply {
                    putParcelable(SONG, song)
                    putBoolean("play", isNow)
                    putInt("po", p)
                    putParcelableArrayList(LIST, ArrayList(songAdapter.getSongList()))

                }
                findNavController(requireView()).navigate(
                    R.id.action_homeFragment_to_playFragment,
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

    private fun onTypeClick() {
        typeAdapter.onItemClicked(object : TypeAdapter.OnItemClick {
            override fun onClickListener(type: Type) {
                val bundle = Bundle().apply {
                    putParcelable(TYPE, type)
                }
                findNavController(requireView()).navigate(
                    R.id.action_homeFragment_to_songTypeFragment,
                    bundle
                )
            }
        })
    }

    private fun onSingerClick() {
        singerAdapter.onItemClicked(object : SingerAdapter.OnItemClick {
            override fun onClickListener(singer: Singer) {
                val bundle = Bundle().apply {
                    putParcelable(SINGER, singer)
                }
                findNavController(requireView()).navigate(
                    R.id.action_homeFragment_to_artistFragment,
                    bundle
                )
            }
        })
    }
}