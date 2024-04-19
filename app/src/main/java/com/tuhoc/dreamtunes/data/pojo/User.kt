package com.tuhoc.dreamtunes.data.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class User(
    @SerializedName("userId")
    val userId: Int?,
    @SerializedName("userName")
    val userName: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("image")
    val image: String?
): Parcelable
