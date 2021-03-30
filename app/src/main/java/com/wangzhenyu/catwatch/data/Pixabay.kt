package com.wangzhenyu.catwatch.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pixabay(
    val hits: ArrayList<Hit>,
    val total: Int,
    val totalHits: Int
) : Parcelable

@Parcelize
data class Hit(
    val comments: Int = 0,
    val downloads: Int = 0,
    val favorites: Int = 0,
    val id: Int = 0,
    val imageHeight: Int = 0,
    val imageSize: Int = 0,
    val imageWidth: Int = 0,
    //大的图片
    val largeImageURL: String = "",
    val likes: Int = 0,
    val pageURL: String = "",
    val previewHeight: Int = 0,
    val previewURL: String = "",
    val previewWidth: Int = 0,
    val tags: String = "",
    val type: String = "",
    val user: String = "",
    val userImageURL: String = "",
    val user_id: Int = 0,
    val views: Int = 0,
    val webformatHeight: Int = 0,
    //中等图片(网络格式)
    val webformatURL: String = "",
    val webformatWidth: Int = 0
) : Parcelable