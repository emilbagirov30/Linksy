package com.emil.linksy.presentation.ui.navigation.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emil.linksy.util.replaceFragment

import com.emil.presentation.databinding.FragmentFeedBinding


class FeedFragment : Fragment() {
    private lateinit var binding: FragmentFeedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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


        }
    }
}