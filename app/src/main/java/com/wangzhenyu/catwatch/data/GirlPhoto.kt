package com.wangzhenyu.catwatch.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GirlPhoto(
    val url: String
) : Parcelable

