package com.tuhoc.dreamtunes.ui.play

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuhoc.dreamtunes.data.pojo.HistoryListenFavorite
import com.tuhoc.dreamtunes.data.retrofit.RetrofitInstance
import com.tuhoc.dreamtunes.utils.Constants
import kotlinx.coroutines.launch

class PlayViewModel: ViewModel() {
    private val songApi = RetrofitInstance.getSongApi

    fun isFavoriteExits(userId: Int, songId: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = songApi.checkFavoriteExists(userId, songId)
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
                updateFavoriteSong(userId, songId, Constants.date(), false)
                onResult(it)
            } else {
                updateFavoriteSong(userId, songId, Constants.date(), true)
                onResult(it)
            }
        }
    }

    private fun updateFavoriteSong(userId: Int, songId: Int, latestListenTime: String, isFavorite: Boolean) {
        val favorite = HistoryListenFavorite(null, null, latestListenTime, isFavorite)
        viewModelScope.launch {
            try {
                songApi.updateFavoriteSong(userId, songId, favorite)
            } catch (e: Exception) {
                // Xử lý exception
                Log.e("TAG", "Error Exception: ${e.message}")
            }
        }
    }
}