package com.tuhoc.dreamtunes.manager

import android.content.Context
import com.google.gson.Gson
import com.tuhoc.dreamtunes.data.pojo.User
import com.tuhoc.dreamtunes.utils.Constants
import com.tuhoc.dreamtunes.utils.Constants.LOGGED_USER

object LoginManager {
    fun checkUserLoggedIn(context: Context): Boolean {
        // Đọc trạng thái đăng nhập từ SharedPreferences
        val sharedPreferences = context.getSharedPreferences(Constants.LOGIN_STATUS, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constants.IS_LOGGED_IN, false)
    }

    fun setLoggedIn(context: Context, isLoggedIn: Boolean, user: User?) {
        val sharedPreferences = context.getSharedPreferences(Constants.LOGIN_STATUS, Context.MODE_PRIVATE)
//        sharedPreferences.edit().putBoolean(Constants.IS_LOGGED_IN, isLoggedIn).apply()

        val editor = sharedPreferences.edit()
        editor.putBoolean(Constants.IS_LOGGED_IN, isLoggedIn)
        if (isLoggedIn && user != null) {
            val gson = Gson()
            val userJson = gson.toJson(user)
            editor.putString(LOGGED_USER, userJson)
        }
        editor.apply()
    }

    fun getCurrentUser(context: Context): User? {
        val sharedPreferences = context.getSharedPreferences(Constants.LOGIN_STATUS, Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean(Constants.IS_LOGGED_IN, false)
        if (!isLoggedIn) {
            return null
        }
        val gson = Gson()
        val userJson = sharedPreferences.getString(LOGGED_USER, null)
        return gson.fromJson(userJson, User::class.java)
    }
}