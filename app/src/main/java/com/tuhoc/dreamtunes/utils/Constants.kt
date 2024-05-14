package com.tuhoc.dreamtunes.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tuhoc.dreamtunes.R
import com.tuhoc.dreamtunes.ui.search.SearchFragment
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object Constants { // lưu trữ những gì liên quan đến key: baseurl, link...
    const val API_KEY = ""
    const val BASE_URL = "http://192.168.1.145:8080/api/"
    const val ALL_SONG_URL = "songs"
    const val ALL_TYPE_URL = "songs/type"
    const val ALL_USER_URL = "users"
    const val ALL_SINGER_URL = "singers"
    const val ALL_FAVORITE_URL = "favorite/"
    const val ALL_HISTORY_LISTEN_URL = "history-listen/"
    const val INSERT_USER_URL = "users/post"
    const val INSERT_PLAYLIST_URL = "users/playlist/post"
    const val INSERT_SONG_BY_PLAYLIST_URL = "users/playlist-song/post"
    const val INSERT_HISTORY_LISTEN_URL = "history-listen/post"
    const val DELETE_PLAYLIST_URL = "users/playlist/delete/"
    const val UPDATE_FAVORITE_URL = "favorite/put/"
    const val DELETE_SONG_BY_PLAYLIST_URL = "users/playlist-song/delete/"
    const val UPDATE_USER_URL = "users/put/"
    const val UPDATE_PLAYLIST_URL = "users/playlist/put/"
    const val UPDATE_HISTORY_LISTEN_URL = "history-listen/put/"
    const val PLAYLIST_BY_USER_URL = "users/playlist/"
    const val SONG_BY_SINGER_URL = "songs/singer/"
    const val SONG_BY_TYPE_URL = "songs/type/"
    const val SONG_BY_ALBUM_URL = "songs/album/"
    const val SONG_BY_PLAYLIST_URL = "users/playlist-song/"
    const val ALBUM_BY_SINGER_URL = "songs/albums/"
    const val USER_BY_ID_URL = "users/"
    const val SONG = "SONG"
    const val ALBUM = "ALBUM"
    const val TYPE = "TYPE"
    const val SINGER = "SINGER"
    const val PLAYLIST = "PLAYLIST"
    const val LIST = "LIST"
    const val CHECK_EMAIL = "users/check-email"
    const val CHECK_FAVORITE = "check-favorite/"
    const val CHECK_HISTORY_LISTEN = "check-history-listen/"
    const val LOGIN_STATUS = "LOGIN_STATUS"
    const val IS_LOGGED_IN = "IS_LOGGED_IN"
    const val LOGGED_USER = "LOGGED_USER"
    const val CHANNEL_ID = "CHANNEL_ID"
    const val SEND_EMAIL = "send-mail"

    fun formatDuration(duration: Long):String {
        val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) -
                minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%2d:%02d", minutes, seconds)
    }

    // ẩn thanh bottom navigation khi hiện keyboard
    fun hideBottomUpKeyboard(activity: FragmentActivity) {
        KeyboardVisibilityEvent.setEventListener(
            activity,
            KeyboardVisibilityEventListener { isOpen ->
                if (isOpen) {
                    activity.findViewById<BottomNavigationView>(R.id.bnvNav).visibility = View.GONE
                } else {
                    val navHostFragment =
                        activity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                    val currentFragment = navHostFragment.childFragmentManager.fragments.firstOrNull()
                    if (currentFragment is SearchFragment) {
                        activity.findViewById<BottomNavigationView>(R.id.bnvNav).visibility = View.VISIBLE
                    } else {
                        activity.findViewById<BottomNavigationView>(R.id.bnvNav).visibility = View.GONE
                    }
                }
            }
        )
    }

    // ẩn bàn phím khi sang màn khác
    fun hideKeyboardOnStart(context: Context, editText: View) {
        val mgr = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mgr.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    // lấy ngày hiện tại
    fun date(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH)
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
}