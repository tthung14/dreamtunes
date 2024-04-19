package com.tuhoc.dreamtunes.ui.play

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuhoc.dreamtunes.data.pojo.Favorite
import com.tuhoc.dreamtunes.data.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class PlayViewModel: ViewModel() {
    private val songApi = RetrofitInstance.getSongApi

    fun isFavoriteExits(userId: Int, songId: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = songApi.checkFavoriteExistence(userId, songId)
                if (response.isSuccessful) {
                    response.body()?.let { onResult(it) }
                } else {
                    response.body()?.let { onResult(it) }
                }
            } catch (e: Exception) {
                Log.e("TAG", "Error Exception: ${e.message}")
            }
        }
    }

    fun isFavorite(userId: Int, songId: Int, onResult: (Boolean) -> Unit) {
        isFavoriteExits(userId, songId) {
            if (it) {
                deleteFavoriteSong(userId, songId)
                onResult(it)
            } else {
                addFavoriteSong(userId, songId)
                onResult(it)
            }
        }
    }

    private fun addFavoriteSong(userId: Int, songId: Int) {
        val favorite = Favorite(songId, userId)
        viewModelScope.launch {
            try {
                songApi.addFavoriteSong(favorite)
            } catch (e: Exception) {
                Log.e("TAG", "Error Exception: ${e.message}")
            }
        }
    }

    private fun deleteFavoriteSong(userId: Int, songId: Int) {
        viewModelScope.launch {
            try {
                songApi.deleteFavoriteSong(userId, songId)
            } catch (e: Exception) {
                // Xử lý exception
                Log.e("TAG", "Error Exception: ${e.message}")
            }
        }
    }
}