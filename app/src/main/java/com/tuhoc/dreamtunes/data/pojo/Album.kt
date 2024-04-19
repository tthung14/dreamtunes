package com.tuhoc.dreamtunes.data.pojo


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Album(
    @SerializedName("albumId")
    val albumId: Int?,
    @SerializedName("albumName")
    val albumName: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("singer")
    val singer: Singer?
): Parcelable