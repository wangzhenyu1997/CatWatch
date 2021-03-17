package com.wangzhenyu.catwatch.fragment

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.wangzhenyu.catwatch.R
import com.wangzhenyu.catwatch.data.GirlPhoto
import com.wangzhenyu.catwatch.databinding.PhotoFragmentBinding
import com.wangzhenyu.catwatch.util.LogUtil

class PhotoFragment : Fragment() {

    private lateinit var viewModel: PhotoViewModel
    private lateinit var binding: PhotoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.photo_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PhotoViewModel::class.java)


        binding.photoFragmentShimmerLayout.apply {
            setShimmerColor(0xFF36DD38.toInt())
            setShimmerAngle(22)
            startShimmerAnimation()
        }

        Glide.with(requireContext())
            .load(arguments?.getParcelable<GirlPhoto>("Photo")?.url.toString())
            .placeholder(R.drawable.ic_loading)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
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
                        binding.photoFragmentShimmerLayout.stopShimmerAnimation()
                    }
                }
            })
            .into(binding.photoFragmentImageView)

    }

}