package com.emil.linksy.presentation.ui.navigation.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.emil.linksy.adapters.RecommendationsAdapter
import com.emil.linksy.presentation.viewmodel.FeedViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.hide
import com.emil.linksy.util.replaceFragment
import com.emil.presentation.databinding.FragmentFeedBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class FeedFragment : Fragment() {
    private lateinit var binding: FragmentFeedBinding
    private val feedViewModel: FeedViewModel by viewModel<FeedViewModel>()
    private val tokenManager: TokenManager by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedBinding.inflate(layoutInflater)
        return  binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val containerId = binding.flContainer.id
        if (containerId != View.NO_ID) {
            replaceFragment(containerId, ChannelPostsFeedFragment())
            binding.chipChannel.isChecked = true
            binding.chipUser.setOnClickListener {
                replaceFragment(containerId, SubscriptionsPostsFeedFragment())
            }
            binding.chipChannel.setOnClickListener {
                replaceFragment(containerId, ChannelPostsFeedFragment())
            }
            binding.rvRecommendations.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
           feedViewModel.getRecommendation(tokenManager.getAccessToken())
           feedViewModel.recommendationList.observe(requireActivity()){list->
               if(list.isEmpty()) binding.tvRecommendationsTitle.hide()
               else
               binding.rvRecommendations.adapter = RecommendationsAdapter(list)
           }
        }
    }
}