package com.tuhoc.dreamtunes.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuhoc.dreamtunes.data.pojo.Singer
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.data.pojo.Type
import com.tuhoc.dreamtunes.data.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val songApi = RetrofitInstance.getSongApi
    private val sliderList = MutableLiveData<List<String>>()

    private val _types = MutableLiveData<List<Type>>()
    val types: LiveData<List<Type>> get() = _types

    private val _singers = MutableLiveData<List<Singer>>()
    val singers: LiveData<List<Singer>> get() = _singers

    private val _randomSongs = MutableLiveData<List<Song>>()
    val randomSongs: LiveData<List<Song>> get() = _randomSongs

    init {
        val listImage = listOf(
            "https://bizweb.dktcdn.net/100/160/353/themes/903213/assets/slider_1.jpg?1688461513259",
            "https://trankhuongmusic.com/public/thumbs/1440x600x1/100_closeup-man-playing-bass-guitar-zyGZeivH9F.webp",
            "https://photo-resize-zmp3.zmdcdn.me/w240_r1x1_jpeg/cover/3/9/8/e/398e9ea699a3d1d0ea28dde20f7d1d99.jpg",
            "https://photo-resize-zmp3.zmdcdn.me/w600_r1x1_jpeg/cover/2/9/0/6/2906681d4b764cd4677342b66813f25d.jpg",
            "https://photo-resize-zmp3.zmdcdn.me/w256_r1x1_jpeg/cover/4/c/e/a/4cea246ebb6a3201636ccae9c75c4521.jpg",
            "https://photo-resize-zmp3.zmdcdn.me/w600_r1x1_jpeg/cover/9/a/1/4/9a14557bd0b957a3863c09f78b038d5d.jpg",
            "https://photo-resize-zmp3.zmdcdn.me/w600_r1x1_jpeg/cover/4/d/7/d/4d7ddbc5c1bebebebfdd08738bf7b5bf.jpg",
        )
        sliderList.value = listImage
    }

    fun getSliders(): LiveData<List<String>> {
        return sliderList
    }

    fun fetchMusicTypes() {
        viewModelScope.launch {
            try {
                val response = songApi.getTypes()
                if (response.isSuccessful) {
                    _types.value = response.body()
                } else {
                    // Xử lý lỗi
                    Log.e("TAG", "Error: ${response.code()}, Message: ${response.message()}")
                }
            } catch (e: Exception) {
                // Xử lý exception
                Log.e("TAG", "Error Exception: ${e.message}")
            }
        }
    }

    fun fetchMusicSingers() {
        viewModelScope.launch {
            try {
                val response = songApi.getSingers()
                if (response.isSuccessful) {
                    _singers.value = response.body()
                } else {
                    // Xử lý lỗi
                    Log.e("TAG", "Error: ${response.code()}, Message: ${response.message()}")
                }
            } catch (e: Exception) {
                // Xử lý exception
                Log.e("TAG", "Error Exception: ${e.message}")
            }
        }
    }

    fun fetchMusicRandomSongs() {
        viewModelScope.launch {
            try {
                val response = songApi.getSongs()
                if (response.isSuccessful) {
                    val allSongs = response.body()
//                    val randomIndexes = (0 until allSongs!!.size).shuffled().take(5)
//                    val randomSongs = randomIndexes.map { allSongs[it] }
                    val positions = listOf(1, 9, 15, 30, 99) // Các vị trí bạn muốn lấy
                    val randomSongs = allSongs!!.filterIndexed { index, _ ->
                        index in positions // Lấy các phần tử ở các vị trí chỉ định
                    }
                    _randomSongs.value = randomSongs

                } else {
                    // Xử lý lỗi
                    Log.e("TAG", "Error: ${response.code()}, Message: ${response.message()}")
                }
            } catch (e: Exception) {
                // Xử lý exception
                Log.e("TAG", "Error Exception: ${e.message}")
            }
        }
    }
}