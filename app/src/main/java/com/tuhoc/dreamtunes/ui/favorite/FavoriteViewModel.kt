package com.tuhoc.dreamtunes.ui.favorite

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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.security.auth.callback.Callback

class FavoriteViewModel: ViewModel() {
    private val songApi = RetrofitInstance.getSongApi

    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> get() = _songs

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> get() = _playlists

    fun getPlaylistsByUser(userId: Int) {
        viewModelScope.launch {
            try {
                val response = songApi.getPlaylistsByUser(userId)
                if (response.isSuccessful) {
                    _playlists.value = response.body()
                } else {
                    Log.e("TAG", "Error: ${response.code()}, Message: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("TAG", "Error Exception: ${e.message}")
            }
        }
    }

    fun getFavoriteSongs(userId: Int) {
        viewModelScope.launch {
            try {
                val response = songApi.getFavoriteSongs(userId)
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

    fun addPlaylist(playlistName: String, dateCreated: String, user: User, callback: (Boolean) -> Unit) {
        val playlist = Playlist(playlistId = null, playlistName = playlistName, dateCreated = dateCreated, user = user)
        viewModelScope.launch {
            try {
                songApi.addPlaylist(playlist)
                callback(true)
            } catch (e: Exception) {
                Log.e("TAG", "Error Exception: ${e.message}")
            }
        }
    }
}