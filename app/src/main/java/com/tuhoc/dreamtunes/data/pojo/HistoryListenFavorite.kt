package com.tuhoc.dreamtunes.data.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryListenFavorite (
    @SerializedName("songId")
    val songId: Int?,
    @SerializedName("userId")
    val userId: Int?,
    @SerializedName("latestListenTime")
    val latestListenTime: String?,
    @SerializedName("isFavorite")
    val isFavorite: Boolean?
): Parcelable