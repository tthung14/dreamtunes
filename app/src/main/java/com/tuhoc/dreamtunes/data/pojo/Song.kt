package com.tuhoc.dreamtunes.data.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Song(
    @SerializedName("songId")
    val songId: Int?,
    @SerializedName("songName")
    val songName: String?,
    @SerializedName("duration")
    val duration: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("link")
    val link: String?,
    @SerializedName("type")
    val type: Type?,
    @SerializedName("album")
    val album: Album?,
    @SerializedName("singer")
    val singer: Singer?
): Parcelable
