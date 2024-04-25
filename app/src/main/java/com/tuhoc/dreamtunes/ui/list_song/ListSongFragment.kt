package com.tuhoc.dreamtunes.ui.list_song

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tuhoc.dreamtunes.R
import com.tuhoc.dreamtunes.adapter.PlaylistSongAddAdapter
import com.tuhoc.dreamtunes.bases.BaseFragment
import com.tuhoc.dreamtunes.data.pojo.Playlist
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.databinding.FragmentListSongBinding
import com.tuhoc.dreamtunes.manager.LoginManager
import com.tuhoc.dreamtunes.ui.home.HomeFragment
import com.tuhoc.dreamtunes.ui.home.HomeViewModel
import com.tuhoc.dreamtunes.ui.play.SharedViewModel
import com.tuhoc.dreamtunes.utils.Constants
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class ListSongFragment : BaseFragment<FragmentListSongBinding>(FragmentListSongBinding::inflate) {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var listSongViewModel: ListSongViewModel
    private lateinit var playlistSongAddAdapter: PlaylistSongAddAdapter
    private lateinit var sharedViewModel: SharedViewModel

    override fun observerData() {
        super.observerData()
        listSongViewModel = ViewModelProvider(this)[ListSongViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        sharedViewModel.messageLiveData.observe(viewLifecycleOwner, Observer { songId ->
            HomeFragment.currentSongId = songId
            playlistSongAddAdapter.updateSong()
        })

        observeSongs()
    }

    override fun initData() {
        super.initData()
        playlistSongAddAdapter = PlaylistSongAddAdapter()
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

        Constants.hideBottomUpKeyboard(requireActivity())
    }

    override fun handleEvent() {
        super.handleEvent()
        onSongClick()
        onClick()
    }

    private fun prepareRecyclerView() {
        binding.apply {
            rcvSong.adapter = playlistSongAddAdapter
        }
    }

    private fun observeSongs() {
        listSongViewModel.fetchMusicSongs()
        listSongViewModel.songs.observe(viewLifecycleOwner, Observer {
            playlistSongAddAdapter.setSongList(it)
        })
    }

    private fun onSongClick() {
        val playlist = arguments?.getParcelable<Playlist>(Constants.PLAYLIST)
        val user = LoginManager.getCurrentUser(requireContext())

        playlistSongAddAdapter.onItemClicked(object : PlaylistSongAddAdapter.OnItemClick {
            override fun onClickListener(song: Song, p: Int, isNow: Boolean) {
                // Chuyển đến màn hình play music khi được click
                val bundle = Bundle().apply {
                    putParcelable(Constants.SONG, song)
                    putBoolean("play", isNow)
                    putInt("po", p)
                    putParcelableArrayList(Constants.LIST, ArrayList(playlistSongAddAdapter.getSongList()))
                }
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_listSongFragment_to_playFragment,
                    bundle
                )

                Constants.hideKeyboardOnStart(requireContext(), binding.edtSearch)

                user!!.userId?.let { song.songId?.let { it1 ->
                    homeViewModel.latestListenTime(it,
                        it1
                    )
                } }
            }

            override fun onClickEvent(song: Song) {
                playlist!!.playlistId?.let {
                    song.songId?.let { it1 ->
                        listSongViewModel.addSongByPlaylist(it, it1) { message ->
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun filter(text: String) {
        // Tạo một danh sách mới để lưu trữ các bài hát đã lọc
        val filteredList: ArrayList<Song> = ArrayList()

        // Chuyển đổi văn bản tìm kiếm và tên bài hát về chữ thường không dấu để so sánh
        val searchText = text.toLowerCase().unaccent()

        // Lọc các bài hát dựa trên văn bản tìm kiếm
        listSongViewModel.songs.observe(viewLifecycleOwner, Observer {
            for (item in it) {
                // Chuyển đổi tên bài hát về chữ thường không dấu để so sánh
                val songName = item.songName?.toLowerCase()?.unaccent()
                // Kiểm tra xem chuỗi nhập vào có tồn tại trong tên bài hát không
                if (songName?.contains(searchText) == true) {
                    // Nếu có, thêm bài hát vào danh sách đã lọc
                    filteredList.add(item)
                }
            }
            // Hiển thị thông báo nếu không tìm thấy dữ liệu
            if (filteredList.isEmpty()) {
//                Toast.makeText(requireContext(), "Không tìm thấy dữ liệu..", Toast.LENGTH_SHORT).show()
            } else {
                // Nếu có dữ liệu, cập nhật danh sách bài hát hiển thị trên RecyclerView
                playlistSongAddAdapter.filterList(filteredList)
            }
        })
    }

    // Phương thức mở rộng để chuyển đổi chuỗi sang chữ thường không dấu
    private fun String.unaccent(): String {
        val temp = java.text.Normalizer.normalize(this, java.text.Normalizer.Form.NFD)
        return Regex("\\p{InCombiningDiacriticalMarks}+").replace(temp, "")
    }

}