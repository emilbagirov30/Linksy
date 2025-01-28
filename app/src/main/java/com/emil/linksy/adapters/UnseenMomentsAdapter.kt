package com.emil.linksy.adapters


import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.emil.domain.model.UnseenSubscriptionMomentResponse
import com.emil.linksy.presentation.ui.FullScreenMomentDialogFragment
import com.emil.linksy.presentation.ui.navigation.feed.SubscriptionsPostsFeedFragment
import com.emil.linksy.presentation.viewmodel.MomentViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.hide
import com.emil.linksy.util.show

import com.emil.presentation.databinding.RvItemUnseenMomentBinding

class UnseenMomentsAdapter (private val list: List<UnseenSubscriptionMomentResponse>,
                              private val context: Context,private val momentViewModel: MomentViewModel,private val tokenManager: TokenManager,
                              private val subscriptionsPostsFeedFragment: SubscriptionsPostsFeedFragment
): RecyclerView.Adapter<UnseenMomentsAdapter.UnseenMomentViewHolder>() {

    inner class UnseenMomentViewHolder(private val binding: RvItemUnseenMomentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(unseenMoment: UnseenSubscriptionMomentResponse) {

            binding.tvName.text = unseenMoment.username
            if(unseenMoment.confirmed) binding.ivConfirmed.show() else binding.ivConfirmed.visibility = View.INVISIBLE
            if (unseenMoment.avatarUrl!="null") {
                Glide.with(context)
                    .load(unseenMoment.avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivAvatar)
            }

            binding.ivAvatar.setOnClickListener {
                FullScreenMomentDialogFragment(userId = unseenMoment.id, unseen = true, position = 0, subscriptionsPostsFeedFragment = subscriptionsPostsFeedFragment).show(
                            subscriptionsPostsFeedFragment.parentFragmentManager,
                            " FullScreenMomentDialogFragment"
                        )

        }
    }}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnseenMomentViewHolder {
        val binding =  RvItemUnseenMomentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return   UnseenMomentViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: UnseenMomentViewHolder, position: Int) {
        holder.bind (list[position])
    }

}