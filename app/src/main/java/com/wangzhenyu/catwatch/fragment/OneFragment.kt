package com.wangzhenyu.catwatch.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.wangzhenyu.catwatch.R
import com.wangzhenyu.catwatch.adapter.FooterAdapter
import com.wangzhenyu.catwatch.adapter.HitAdapter
import com.wangzhenyu.catwatch.databinding.FOneFragmentBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OneFragment : Fragment() {


    private lateinit var viewModel: OneViewModel
    private lateinit var binding: FOneFragmentBinding

    private val adapter = HitAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.f_one_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OneViewModel::class.java)

        binding.fOneFragmentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.fOneFragmentRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.getPagingData().collect {
                adapter.submitData(it)
            }
        }

        adapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    binding.fOneFragmentProgressBar.visibility = View.INVISIBLE
                    binding.fOneFragmentRecyclerView.visibility = View.VISIBLE
                }
                is LoadState.Loading -> {
                    binding.fOneFragmentProgressBar.visibility = View.VISIBLE
                    binding.fOneFragmentRecyclerView.visibility = View.INVISIBLE
                }
                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error
                    binding.fOneFragmentProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(
                        requireContext(),
                        "Load Error: ${state.error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.fOneFragmentRecyclerView.adapter = adapter.withLoadStateFooter(FooterAdapter {
            adapter.retry()
        })

    }

}