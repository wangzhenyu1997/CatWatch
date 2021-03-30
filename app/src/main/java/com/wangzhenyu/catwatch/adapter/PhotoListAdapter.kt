package com.wangzhenyu.catwatch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wangzhenyu.catwatch.R
import com.wangzhenyu.catwatch.data.Hit


class PhotoListAdapter : ListAdapter<Hit, PhotoListAdapter.VH>(diff) {

    companion object {
        private val diff = object : DiffUtil.ItemCallback<Hit>() {
            override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
                return oldItem.largeImageURL == newItem.largeImageURL
            }

        }
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.item_photo_list_cell_imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo_list_cell, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        Glide.with(holder.itemView)
            .load(getItem(position).largeImageURL)
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.one)
            .into(holder.imageView)
    }


}