package com.emil.linksy.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.RecommendationResponse
import com.emil.linksy.presentation.ui.page.ChannelPageActivity
import com.emil.linksy.presentation.ui.page.UserPageActivity
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.emil.presentation.databinding.RvItemRecommendationsBinding

class RecommendationsAdapter (private val list: List<RecommendationResponse>
): RecyclerView.Adapter<RecommendationsAdapter.RecommendationViewHolder>() {

    inner class RecommendationViewHolder(private val binding: RvItemRecommendationsBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bind(recommendation: RecommendationResponse) {
            binding.ivConfirmed.hide()
            binding.tvLink.hide()
            if(recommendation.confirmed) binding.ivConfirmed.show()
            binding.tvName.text = recommendation.name
            if (recommendation.link!=null){
                binding.tvLink.show()
                binding.tvLink.text = "@${recommendation.link}"
            }

            if (recommendation.avatarUrl != Linksy.EMPTY_AVATAR){
                Glide.with(context)
                    .load(recommendation.avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivAvatar)
            }

            if (recommendation.channelId!=null){
                if (recommendation.avatarUrl == Linksy.EMPTY_AVATAR) binding.ivAvatar.setBackgroundResource(R.drawable.default_channel_avatar)
                binding.tvLink.setTextColor(ContextCompat.getColor(context, R.color.green))
                 binding.root.setOnClickListener {
                     val switchingToChannelPageActivity =
                         Intent(context, ChannelPageActivity()::class.java)
                     switchingToChannelPageActivity.putExtra(Linksy.INTENT_CHANNEL_ID_KEY, recommendation.channelId)
                     context.startActivity(switchingToChannelPageActivity)
                 }
            } else {
                if (recommendation.avatarUrl == Linksy.EMPTY_AVATAR) binding.ivAvatar.setBackgroundResource(R.drawable.default_avatar)
                binding.tvLink.setTextColor(ContextCompat.getColor(context, R.color.link))

                binding.root.setOnClickListener {
                    val switchingToUserPageActivity = Intent(context, UserPageActivity::class.java)
                    switchingToUserPageActivity.putExtra(Linksy.INTENT_USER_ID_KEY, recommendation.userId)
                    context.startActivity(switchingToUserPageActivity)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding =  RvItemRecommendationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  RecommendationViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bind (list[position])
    }

}