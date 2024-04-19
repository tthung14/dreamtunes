package com.tuhoc.dreamtunes.data.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistSong (
    @SerializedName("songId")
    val songId: Int?,
    @SerializedName("playlistId")
    val playlistId: Int?,
): Parcelable