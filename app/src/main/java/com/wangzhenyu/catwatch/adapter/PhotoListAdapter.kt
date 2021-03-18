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
import com.wangzhenyu.catwatch.data.GirlPhoto

class PhotoListAdapter : ListAdapter<GirlPhoto, PhotoListAdapter.VH>(diff) {

    companion object {
        private val diff = object : DiffUtil.ItemCallback<GirlPhoto>() {
            override fun areItemsTheSame(oldItem: GirlPhoto, newItem: GirlPhoto): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: GirlPhoto, newItem: GirlPhoto): Boolean {
                return oldItem.url == newItem.url
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
            .load(getItem(position).url)
            .placeholder(R.drawable.ic_loading)
            .into(holder.imageView)

    }


}