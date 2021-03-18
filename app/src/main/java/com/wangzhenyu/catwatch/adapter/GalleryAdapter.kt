package com.wangzhenyu.catwatch.adapter

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.wangzhenyu.catwatch.R
import com.wangzhenyu.catwatch.data.GirlPhoto
import com.wangzhenyu.catwatch.util.LogUtil
import io.supercharge.shimmerlayout.ShimmerLayout

class GalleryAdapter : ListAdapter<GirlPhoto, GalleryAdapter.VH>(diff) {

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
        val imageView: ImageView = view.findViewById(R.id.item_girl_imageView)
        val shimmer: ShimmerLayout = view.findViewById(R.id.item_girl_shimmerLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val viewHolder =
            VH(LayoutInflater.from(parent.context).inflate(R.layout.item_girl_cell, parent, false))

        viewHolder.itemView.setOnClickListener {
            Bundle().apply {
                putParcelableArrayList("PhotoList", ArrayList(currentList))
                putInt("PhotoPosition", viewHolder.adapterPosition)
                Navigation.findNavController(it)
                    .navigate(R.id.action_galleryFragment_to_photoFragment, this)
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.shimmer.apply {
            setShimmerColor(0x55FFFFFF)//闪动颜色
            setShimmerAngle(22)//角度
            startShimmerAnimation()
        }

        Glide.with(holder.itemView)
            .load(getItem(position).url)
            .placeholder(R.drawable.ic_loading)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    LogUtil.d("RetrofitGirl", "图片加载失败")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false.also {
                        holder.shimmer?.stopShimmerAnimation()
                    }
                }
            })
            .into(holder.imageView)


    }


}