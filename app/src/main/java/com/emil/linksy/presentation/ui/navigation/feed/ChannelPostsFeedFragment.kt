package com.emil.linksy.presentation.ui.navigation.feed

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.emil.linksy.adapters.ChannelPostsAdapter
import com.emil.linksy.presentation.viewmodel.ChannelViewModel
import com.emil.linksy.presentation.viewmodel.FeedViewModel
import com.emil.linksy.util.TokenManager
import com.emil.presentation.databinding.FragmentChannelPostsFeedBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChannelPostsFeedFragment : Fragment() {
    private lateinit var binding: FragmentChannelPostsFeedBinding
    private val feedViewModel: FeedViewModel by viewModel<FeedViewModel>()
    private val channelViewModel: ChannelViewModel by viewModel<ChannelViewModel>()
    private val tokenManager: TokenManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =FragmentChannelPostsFeedBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       binding.rvPosts.layoutManager = LinearLayoutManager(requireActivity())
        val sharedPref: SharedPreferences = requireActivity().getSharedPreferences("appData", Context.MODE_PRIVATE)
        val userId = sharedPref.getLong("ID",-1)
        getPosts()
        binding.swipeRefreshLayout.setOnRefreshListener {
            getPosts()
        }
        feedViewModel.channelPostList.observe(requireActivity()){postList ->
            binding.rvPosts.adapter =
                context?.let { ChannelPostsAdapter(postList, it,tokenManager,channelViewModel,userId, channelPostsFeedFragment = this) }
        }
    }


    fun getPosts(){
        feedViewModel.getAllChannelPosts(tokenManager.getAccessToken(), onSuccess = {binding.swipeRefreshLayout.isRefreshing=false})
    }

}