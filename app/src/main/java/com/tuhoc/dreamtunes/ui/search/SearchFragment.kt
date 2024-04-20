package com.tuhoc.dreamtunes.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tuhoc.dreamtunes.R
import com.tuhoc.dreamtunes.adapter.SearchAdapter
import com.tuhoc.dreamtunes.bases.BaseFragment
import com.tuhoc.dreamtunes.data.pojo.Singer
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.databinding.FragmentSearchBinding
import com.tuhoc.dreamtunes.ui.home.HomeFragment
import com.tuhoc.dreamtunes.ui.home.HomeViewModel
import com.tuhoc.dreamtunes.ui.list_song.ListSongViewModel
import com.tuhoc.dreamtunes.ui.play.SharedViewModel
import com.tuhoc.dreamtunes.utils.Constants
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {
    private lateinit var listSongViewModel: ListSongViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var sharedViewModel: SharedViewModel

    override fun observerData() {
        super.observerData()
        listSongViewModel = ViewModelProvider(this)[ListSongViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        sharedViewModel.messageLiveData.observe(viewLifecycleOwner, Observer { songId ->
            HomeFragment.currentSongId = songId
            searchAdapter.updateSong()
        })

        observeSongs()
        observeSingers()
    }

    override fun initData() {
        super.initData()
        searchAdapter = SearchAdapter()
    }

    override fun initView() {
        super.initView()
        prepareRecyclerView()

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString()
                filter(searchText)
            }
        })

        KeyboardVisibilityEvent.setEventListener(
            requireActivity(),
            KeyboardVisibilityEventListener { isOpen ->
                if (isOpen) {
                    requireActivity().findViewById<BottomNavigationView>(R.id.bnvNav).visibility = View.INVISIBLE
                } else {
                    requireActivity().findViewById<BottomNavigationView>(R.id.bnvNav).visibility = View.VISIBLE
                }
            })
    }

    override fun handleEvent() {
        super.handleEvent()
        onSongClick()
    }

    private fun prepareRecyclerView() {
        binding.apply {
            rcvSong.adapter = searchAdapter
        }
    }

    private fun observeSongs() {
        listSongViewModel.fetchMusicSongs()
    }

    private fun observeSingers() {
        homeViewModel.fetchMusicSingers()
    }

    private fun onSongClick() {
        searchAdapter.onItemClicked(object : SearchAdapter.OnItemClick {
            override fun onSongClickListener(song: Song, p: Int, isNow: Boolean) {
                val bundle = Bundle().apply {
                    putParcelable(Constants.SONG, song)
                    putBoolean("play", isNow)
                    putInt("po", p)
                    putParcelableArrayList(Constants.LIST, ArrayList(searchAdapter.getSongList().filterIsInstance<Song>()))
                }
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_searchFragment_to_playFragment,
                    bundle
                )
            }

            override fun onSingerClickListener(singer: Singer) {
                val bundle = Bundle().apply {
                    putParcelable(Constants.SINGER, singer)
                }
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_searchFragment_to_artistFragment,
                    bundle
                )
            }
        })

    }

    private fun filter(text: String) {
        val filteredList: ArrayList<Any> = ArrayList()
        val searchText = text.toLowerCase().unaccent()

        // Lọc danh sách bài hát
        listSongViewModel.songs.observe(viewLifecycleOwner, Observer { songs ->
            for (item in songs) {
                val songName = item.songName?.toLowerCase()?.unaccent()
                if (songName?.contains(searchText) == true) {
                    filteredList.add(item)
                }
            }
            if (filteredList.isEmpty()) {
            } else {
                searchAdapter.filterList(filteredList)
            }
        })

        // Lọc danh sách ca sĩ
        homeViewModel.singers.observe(viewLifecycleOwner, Observer { singers ->
            for (item in singers) {
                val singerName = item.singerName?.toLowerCase()?.unaccent()
                if (singerName?.contains(searchText) == true) {
                    filteredList.add(item)
                }
            }
            if (filteredList.isEmpty()) {
            } else {
                searchAdapter.filterList(filteredList)
            }
        })
    }

    private fun String.unaccent(): String {
        val temp = java.text.Normalizer.normalize(this, java.text.Normalizer.Form.NFD)
        return Regex("\\p{InCombiningDiacriticalMarks}+").replace(temp, "")
    }
}