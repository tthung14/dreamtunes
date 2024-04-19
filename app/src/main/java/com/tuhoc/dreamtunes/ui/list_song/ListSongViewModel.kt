package com.tuhoc.dreamtunes.ui.list_song

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuhoc.dreamtunes.data.pojo.Favorite
import com.tuhoc.dreamtunes.data.pojo.Playlist
import com.tuhoc.dreamtunes.data.pojo.PlaylistSong
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.data.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class ListSongViewModel: ViewModel(){
    private val songApi = RetrofitInstance.getSongApi

    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> get() = _songs

    fun fetchMusicSongs() {
        viewModelScope.launch {
            try {
                val response = songApi.getSongs()
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

    fun addSongByPlaylist(playlistId: Int, songId: Int, callback: (String) -> Unit) {
        val playlistSong = PlaylistSong(songId, playlistId)
        viewModelScope.launch {
            try {
                songApi.addSongByPlaylist(playlistSong)
                callback("Thêm bài hát thành công")
            } catch (e: Exception) {
                Log.e("TAG", "Error Exception: ${e.message}")
            }
        }
    }
}