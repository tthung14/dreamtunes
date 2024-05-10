package com.tuhoc.dreamtunes.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuhoc.dreamtunes.data.pojo.User
import com.tuhoc.dreamtunes.data.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val songApi = RetrofitInstance.getSongApi
    private val _users = MutableLiveData<List<User>>()

    fun fetchUsers() {
        viewModelScope.launch {
            try {
                val response = songApi.getUsers()
                if (response.isSuccessful) {
                    _users.value = response.body()
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

    fun logIn(email: String, password: String, onResult: (Boolean, String, User?) -> Unit) {
        // Duyệt danh sách người dùng để kiểm tra
        _users.value?.let { users ->
            val foundUser = users.find { it.email == email }

            if (foundUser != null) {
                if (foundUser.role == "user") {
                    Log.e("TAG", "logIn: "+ foundUser.role)
                    if (foundUser.password == password) {
                        onResult(true, "Login Successful", foundUser)
                    } else {
                        onResult(false, "Incorrect password", null)
                    }
                } else {
                    onResult(false, "Login Failed", null)
                }
            } else {
                onResult(false, "User not found", null)
            }
        } ?: run {
            onResult(false, "Error fetching user data", null)
        }
    }

    fun forgetPassword(email: String, onResult: (Boolean, String) -> Unit) {

    }
}