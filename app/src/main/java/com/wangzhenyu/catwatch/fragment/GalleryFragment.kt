package com.wangzhenyu.catwatch.fragment

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.GeneratedAdapter
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wangzhenyu.catwatch.R
import com.wangzhenyu.catwatch.adapter.GalleryAdapter
import com.wangzhenyu.catwatch.databinding.GalleryFragmentBinding
import com.wangzhenyu.catwatch.util.LogUtil

class GalleryFragment : Fragment() {

    companion object {
        const val TAG = "TESTAAA"
    }

    private lateinit var binding: GalleryFragmentBinding
    private lateinit var viewModel: GalleryViewModel

    private lateinit var galleryAdapter: GalleryAdapter


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.gallery_menu, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        LogUtil.d(TAG, "onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.gallery_fragment, container, false)
        galleryAdapter = GalleryAdapter()

        binding.galleryFragmentRecyclerView.apply {
            adapter = galleryAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        binding.galleryFragmentSwipeRefreshLayout.setOnRefreshListener {
            viewModel.getGirlPhoto("15")

        }


        setHasOptionsMenu(true)

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

        LogUtil.d(TAG, "onActivityCreated")

    }


}