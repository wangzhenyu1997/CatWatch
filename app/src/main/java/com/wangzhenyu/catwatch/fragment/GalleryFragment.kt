package com.wangzhenyu.catwatch.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.wangzhenyu.catwatch.R
import com.wangzhenyu.catwatch.adapter.GalleryAdapter
import com.wangzhenyu.catwatch.databinding.FGalleryFragmentBinding

class GalleryFragment : Fragment() {


    private lateinit var binding: FGalleryFragmentBinding
    private lateinit var viewModel: GalleryViewModel

    private lateinit var galleryAdapter: GalleryAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.f_gallery_fragment, container, false)
        galleryAdapter = GalleryAdapter()

        binding.galleryFragmentRecyclerView.apply {
            adapter = galleryAdapter
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        }

        binding.galleryFragmentSwipeRefreshLayout.setOnRefreshListener {
            viewModel.getGirlPhoto("15")

        }

        // setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)

        viewModel.getGirlPhoto("100")

        viewModel.photoListLiveData.observe(viewLifecycleOwner, Observer {
            galleryAdapter.submitList(it)
            binding.galleryFragmentSwipeRefreshLayout.isRefreshing = false
        })



    }


}