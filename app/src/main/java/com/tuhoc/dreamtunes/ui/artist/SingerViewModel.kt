package com.tuhoc.dreamtunes.ui.artist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuhoc.dreamtunes.data.pojo.Album
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.data.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class SingerViewModel: ViewModel() {
    private val songApi = RetrofitInstance.getSongApi

    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> get() = _songs

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> get() = _albums

    fun getSongsBySinger(singerId: Int) {
        viewModelScope.launch {
            try {
                val response = songApi.getSongsBySinger(singerId)
                if (response.isSuccessful) {
                    _songs.value = response.body()
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

    fun getAlbumsBySinger(singerId: Int) {
        viewModelScope.launch {
            try {
                val response = songApi.getAlbumsBySinger(singerId)
                if (response.isSuccessful) {
                    _albums.value = response.body()
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