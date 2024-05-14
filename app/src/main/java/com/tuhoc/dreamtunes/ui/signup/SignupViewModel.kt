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
        val user = User(null, userName, email, password, "user", null)
        viewModelScope.launch {
            try {
                val response = songApi.addUser(user)
                if (response.isSuccessful) {
                    onResult(true, "Đăng ký thành công")
                } else {
                    onResult(false, "Đăng ký thất bại")
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
                        onResult(false, "Email đã tồn tại")
                    } else {
                        onResult(true, "Email có sẵn để đăng ký")
                    }
                } else {
                    onResult(false, "Kiểm tra email thất bại")
                }
            } catch (e: Exception) {
                onResult(false, "Error occurred: ${e.message}")
            }
        }
    }
}