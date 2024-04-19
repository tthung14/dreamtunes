package com.tuhoc.dreamtunes.data.pojo


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Singer(
    @SerializedName("singerId")
    val singerId: Int?,
    @SerializedName("singerName")
    val singerName: String?,
    @SerializedName("image")
    val image: String?
): Parcelable