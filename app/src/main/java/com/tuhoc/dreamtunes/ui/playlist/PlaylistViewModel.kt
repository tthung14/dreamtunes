package com.tuhoc.dreamtunes.ui.playlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuhoc.dreamtunes.data.pojo.Playlist
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.data.pojo.User
import com.tuhoc.dreamtunes.data.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class PlaylistViewModel: ViewModel() {
    private val songApi = RetrofitInstance.getSongApi

    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> get() = _songs

    fun getSongsByPlaylist(playlistId: Int) {
        viewModelScope.launch {
            try {
                val response = songApi.getSongsByPlaylist(playlistId)
                if (response.isSuccessful) {
                    _songs.value = response.body()
                } else {
                    Log.e("TAG", "Error: ${response.code()}, Message: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("TAG", "Error Exception: ${e.message}")
            }
        }
    }

    fun deletePlaylist(playlistId: Int) {
        viewModelScope.launch {
            try {
                songApi.deletePlaylist(playlistId)
            } catch (e: Exception) {
                Log.e("TAG", "Error Exception: ${e.message}")
            }
        }
    }

    fun updatePlaylist(playlistId: Int, playlistName: String, callback: (Boolean, String) -> Unit) {
        val playlist = Playlist(playlistId = null, playlistName = playlistName, dateCreated = null, user = null)
        viewModelScope.launch {
            try {
                songApi.updatePlaylist(playlistId, playlist)
                callback(true, "Cập nhật thành công")
            } catch (e: Exception) {
                Log.e("TAG", "Error Exception: ${e.message}")
            }
        }
    }

    fun deleteSongByPlaylist(playlistId: Int, songId: Int, callback: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                songApi.deleteSongByPlaylist(playlistId, songId)
                callback(true, "Xoá bài hát thành công")
            } catch (e: Exception) {
                Log.e("TAG", "Error Exception: ${e.message}")
            }
        }
    }
}