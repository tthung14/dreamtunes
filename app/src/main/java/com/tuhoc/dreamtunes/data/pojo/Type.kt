package com.tuhoc.dreamtunes.data.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Type(
    @SerializedName("typeId")
    val typeId: Int?,
    @SerializedName("typeName")
    val typeName: String?,
    @SerializedName("image")
    val image: String?
): Parcelable