package com.tuhoc.dreamtunes.ui.songtype

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.data.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class SongTypeViewModel: ViewModel() {
    private val songApi = RetrofitInstance.getSongApi

    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> get() = _songs

    fun getSongsByType(typeId: Int) {
        viewModelScope.launch {
            try {
                val response = songApi.getSongsByType(typeId)
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

    fun getSongsByAlbum(albumId: Int) {
        viewModelScope.launch {
            try {
                val response = songApi.getSongsByAlbum(albumId)
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
}