package com.wangzhenyu.catwatch.adapter

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.wangzhenyu.catwatch.R
import com.wangzhenyu.catwatch.data.Hit
import com.wangzhenyu.catwatch.util.LogUtil
import io.supercharge.shimmerlayout.ShimmerLayout

class GalleryAdapter : ListAdapter<Hit, GalleryAdapter.VH>(diff) {

    companion object {
        const val NORMAL_VIEW_TYPE = 0
        const val FOOTER_VIEW_TYPE = 1
        private val diff = object : DiffUtil.ItemCallback<Hit>() {
            override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
                return oldItem.largeImageURL == newItem.largeImageURL &&
                        oldItem.webformatURL == newItem.webformatURL
            }
        }
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

//    override fun getItemViewType(position: Int): Int {
//        return if (position == itemCount - 1) FOOTER_VIEW_TYPE else NORMAL_VIEW_TYPE
//    }
//
//    override fun getItemCount(): Int {
//        return super.getItemCount() + 1
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        //  if (viewType == NORMAL_VIEW_TYPE) {
        val holder: VH = VH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_girl_cell, parent, false)
        )
        holder.itemView.setOnClickListener {
            Bundle().apply {
                putParcelableArrayList("PhotoList", ArrayList(currentList))
                putInt("PhotoPosition", holder.absoluteAdapterPosition)
                Navigation.findNavController(it)
                    .navigate(R.id.action_galleryFragment_to_photoFragment, this)
            }
        }
//        } else {
//            holder = VH(
//                LayoutInflater.from(parent.context).inflate(R.layout.gallery_footer, parent, false)
//                    .also {
//                        (it.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
//                            true
//                    }
//            )
//        }
        return holder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
//        if (position == itemCount - 1) {
//            return
//        }
        holder.itemView.findViewById<ShimmerLayout>(R.id.item_girl_shimmerLayout).apply {
            setShimmerColor(0x55FFFFFF)//闪动颜色
            setShimmerAngle(22)//角度
            startShimmerAnimation()
        }
        Glide.with(holder.itemView)
            .load(getItem(position).webformatURL)
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.one)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    LogUtil.d("RetrofitGirl", "图片加载失败")
                    return false.also {
                        holder.itemView
                            .findViewById<ShimmerLayout>(R.id.item_girl_shimmerLayout)
                            ?.stopShimmerAnimation()
                    }
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false.also {
                        holder.itemView
                            .findViewById<ShimmerLayout>(R.id.item_girl_shimmerLayout)
                            ?.stopShimmerAnimation()
                    }
                }
            })
            .into(holder.itemView.findViewById(R.id.item_girl_imageView))
    }
}