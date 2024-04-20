package com.tuhoc.dreamtunes.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaMetadataRetriever
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.color.MaterialColors
import com.tuhoc.dreamtunes.R
import java.util.concurrent.TimeUnit

object Constants { // lưu trữ những gì liên quan đến key: baseurl, link...
    const val API_KEY = ""
    const val BASE_URL = "http://192.168.1.19:8080/api/"
    const val ALL_SONG_URL = "songs"
    const val ALL_TYPE_URL = "songs/type"
    const val ALL_USER_URL = "users"
    const val ALL_SINGER_URL = "singers"
    const val ALL_FAVORITE_URL = "favorites/"
    const val INSERT_USER_URL = "users/post"
    const val INSERT_FAVORITE_URL = "favorites/post"
    const val INSERT_PLAYLIST_URL = "users/playlist/post"
    const val INSERT_SONG_BY_PLAYLIST_URL = "users/playlistsong/post"
    const val DELETE_FAVORITE_URL = "favorites/delete/"
    const val DELETE_PLAYLIST_URL = "users/playlist/delete/"
    const val DELETE_SONG_BY_PLAYLIST_URL = "users/playlistsong/delete/"
    const val UPDATE_USER_URL = "users/put/"
    const val UPDATE_PLAYLIST_URL = "users/playlist/put/"
    const val PLAYLIST_BY_USER_URL = "users/playlist/"
    const val SONG_BY_SINGER_URL = "songs/singer/"
    const val SONG_BY_TYPE_URL = "songs/type/"
    const val SONG_BY_ALBUM_URL = "songs/album/"
    const val SONG_BY_PLAYLIST_URL = "users/playlistsong/"
    const val ALBUM_BY_SINGER_URL = "songs/albums/"
    const val USER_BY_ID_URL = "users/"
    const val SONG = "SONG"
    const val ALBUM = "ALBUM"
    const val TYPE = "TYPE"
    const val SINGER = "SINGER"
    const val PLAYLIST = "PLAYLIST"
    const val LIST = "LIST"
    const val CHECK_EMAIL = "users/checkemail"
    const val CHECK_FAVORITE = "favorites/checkfavorite/"
    const val LOGIN_STATUS = "LOGIN_STATUS"
    const val IS_LOGGED_IN = "IS_LOGGED_IN"
    const val LOGGED_USER = "LOGGED_USER"
    const val CHANNEL_ID = "CHANNEL_ID"
    const val DATABASE_URL = "https://shopsmart-66e02-default-rtdb.asia-southeast1.firebasedatabase.app/"

    fun formatDuration(duration: Long):String {
        val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) -
                minutes* TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%2d:%02d", minutes, seconds)
    }
}