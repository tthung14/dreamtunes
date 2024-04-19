package com.tuhoc.dreamtunes.ui.signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuhoc.dreamtunes.data.pojo.User
import com.tuhoc.dreamtunes.data.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {
    private val songApi = RetrofitInstance.getSongApi

    fun signUp(userName: String, email: String, password: String, onResult: (Boolean, String) -> Unit) {
        val user = User(null, userName, email, password, null)
        viewModelScope.launch {
            try {
                val response = songApi.addUser(user)
                if (response.isSuccessful) {
                    onResult(true, "Signup Successful")
                } else {
                    onResult(false, "Failed to signup")
                }
            } catch (e: Exception) {
                onResult(false, "Error occurred: ${e.message}")
            }
        }
    }

    fun checkEmailExistence(email: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = songApi.checkEmailExistence(email)
                if (response.isSuccessful) {
                    val emailExists = response.body() ?: false
                    if (emailExists) {
                        onResult(false, "Email already exists")
                    } else {
                        onResult(true, "Email available for signup")
                    }
                } else {
                    onResult(false, "Failed to check email existence")
                }
            } catch (e: Exception) {
                onResult(false, "Error occurred: ${e.message}")
            }
        }
    }
}