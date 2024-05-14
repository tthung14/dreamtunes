package com.tuhoc.dreamtunes.ui.edit_profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tuhoc.dreamtunes.data.pojo.User
import com.tuhoc.dreamtunes.data.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class EditProfileViewModel: ViewModel() {
    private val storageRef = FirebaseStorage.getInstance().reference
    private val songApi = RetrofitInstance.getSongApi

    fun editPhoto(userId: Int, photoUri: Uri, onResult: (String, Boolean) -> Unit) {
        val imageRef: StorageReference = storageRef.child("DreamTunes/NguoiDung/${userId}.jpg")

        imageRef.putFile(photoUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    val user = User(null, null, null, null, null, it.toString())
                    viewModelScope.launch {
                        try {
                            val response = songApi.updateUser(userId, user)
                            if (response.isSuccessful) {
                                onResult("Chỉnh sửa thành công", true)
                            } else {
                                onResult("Chỉnh sửa thất bại", false)
                            }
                        } catch (e: Exception) {
                            onResult("Error occurred: ${e.message}", false)
                        }
                    }
                }.addOnFailureListener { e ->
                    onResult("Get Image failure: ${e.message}", false)
                }
            }
            .addOnFailureListener { e ->
                onResult("Image upload failure: ${e.message}", false)
            }
    }

    fun editUserName(userId: Int, name: String, onResult: (String, Boolean) -> Unit) {
        val user = User(null, name, null, null, null,null)
        viewModelScope.launch {
            try {
                val response = songApi.updateUser(userId, user)
                if (response.isSuccessful) {
                    onResult("Chỉnh sửa thành công", true)
                } else {
                    onResult("Chỉnh sửa thất bại", false)
                }
            } catch (e: Exception) {
                onResult("Error occurred: ${e.message}", false)
            }
        }
    }

    fun editPassword(userId: Int, oldPass: String, newPass: String, onResult: (String, Boolean) -> Unit) {
        val user = User(null, null, newPass, null, null, null)
        viewModelScope.launch {
            try {
                val response = songApi.updateUser(userId, user)
                if (response.isSuccessful) {
                    onResult("Chỉnh sửa thành công", true)
                } else {
                    onResult("Chỉnh sửa thất bại", false)
                }
            } catch (e: Exception) {
                onResult("Error occurred: ${e.message}", false)
            }
        }
    }
}