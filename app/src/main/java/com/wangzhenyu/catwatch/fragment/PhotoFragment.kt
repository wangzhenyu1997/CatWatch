package com.wangzhenyu.catwatch.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.wangzhenyu.catwatch.R
import com.wangzhenyu.catwatch.adapter.PhotoListAdapter
import com.wangzhenyu.catwatch.data.GirlPhoto
import com.wangzhenyu.catwatch.databinding.FPhotoFragmentBinding
import com.wangzhenyu.catwatch.util.LogUtil
import java.util.ArrayList

class PhotoFragment : Fragment() {

    private lateinit var viewModel: PhotoViewModel
    private lateinit var binding: FPhotoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.f_photo_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PhotoViewModel::class.java)


        val list: ArrayList<GirlPhoto> = arguments?.getParcelableArrayList<GirlPhoto>("PhotoList")!!

        binding.photoFragmentTextView.text =
            "${arguments?.getInt("PhotoPosition")?.plus(1) ?: 0}/${list.size ?: 0}"

        PhotoListAdapter().let {
            binding.photoFragmentViewPager2.adapter = it
            it.submitList(list)
        }



        binding.photoFragmentViewPager2.apply {
            setCurrentItem(
                arguments?.getInt("PhotoPosition") ?: 0,
                false
            )

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.photoFragmentTextView.text =
                        "${position + 1 ?: 0}/${list.size ?: 0}"

                }
            })

        }


    }

}