package com.wangzhenyu.catwatch.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wangzhenyu.catwatch.R
import com.wangzhenyu.catwatch.data.Hit
import com.wangzhenyu.catwatch.util.MyApplication
import kotlinx.coroutines.currentCoroutineContext

class HitAdapter : PagingDataAdapter<Hit, HitAdapter.ViewHolder>(diff) {

    companion object {
        val diff = object : DiffUtil.ItemCallback<Hit>() {
            override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
                return oldItem == newItem
            }

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_girl_imageView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(getItem(position)?.webformatURL)
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.one)
            .into(holder.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_girl_cell, parent, false))



        return viewHolder
    }



}