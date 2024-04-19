package com.tuhoc.dreamtunes.data.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Playlist(
    @SerializedName("playlistId")
    val playlistId: Int?,
    @SerializedName("playlistName")
    val playlistName: String?,
    @SerializedName("dateCreated")
    val dateCreated: String?,
    @SerializedName("user")
    val user: User?
): Parcelable
