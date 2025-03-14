package com.emil.linksy.presentation.ui.navigation.feed

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.emil.linksy.adapters.PostsAdapter
import com.emil.linksy.adapters.UnseenMomentsAdapter
import com.emil.linksy.presentation.viewmodel.FeedViewModel
import com.emil.linksy.presentation.viewmodel.PostViewModel
import com.emil.linksy.util.TokenManager

import com.emil.presentation.databinding.FragmentSubscriptionsPostsFeedBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SubscriptionsPostsFeedFragment : Fragment() {
    private lateinit var binding:FragmentSubscriptionsPostsFeedBinding
    private val feedViewModel: FeedViewModel by viewModel<FeedViewModel>()
    private val postViewModel: PostViewModel by viewModel<PostViewModel>()
    private val tokenManager: TokenManager by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSubscriptionsPostsFeedBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPosts.layoutManager = LinearLayoutManager(requireActivity())

        feedViewModel.subscriptionPostList.observe(requireActivity()){postList ->
            binding.rvPosts.adapter =
                PostsAdapter(postList,postViewModel, tokenManager, subscriptionsPostsFeedFragment = this)
        }
        binding.rvMoments.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        feedViewModel.unseenMomentList.observe(requireActivity()){list->
            binding.rvMoments.adapter = UnseenMomentsAdapter (list,requireActivity(),this)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            getData()
        }
        getData()
    }

    fun getData(){
        feedViewModel.getAllSubscriptionsPosts(tokenManager.getAccessToken(), onSuccess = {})
        feedViewModel.getAllUnseenMoments(tokenManager.getAccessToken())
        binding.swipeRefreshLayout.isRefreshing = false
    }
}