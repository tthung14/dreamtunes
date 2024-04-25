package com.tuhoc.dreamtunes.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.data.pojo.User
import com.tuhoc.dreamtunes.data.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    private val songApi = RetrofitInstance.getSongApi

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> get() = _songs


    fun getUserById(userId: Int) {
        viewModelScope.launch {
            try {
                val response = songApi.getUserById(userId)
                if (response.isSuccessful) {
                    _user.value = response.body()
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

    fun getHistoryListen(userId: Int) {
        viewModelScope.launch {
            try {
                val response = songApi.getHistoryListen(userId)
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
}