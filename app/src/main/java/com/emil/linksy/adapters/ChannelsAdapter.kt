package com.emil.linksy.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.ChannelResponse
import com.emil.domain.model.ChannelType
import com.emil.linksy.presentation.ui.page.ChannelPageActivity
import com.emil.linksy.util.colorByRating
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.presentation.R

import com.emil.presentation.databinding.RvItemChannelBinding

class ChannelsAdapter(
    private val channelList: List<ChannelResponse>,
    private val context: Context,
    private val userId: Long
) : RecyclerView.Adapter<ChannelsAdapter.ChannelViewHolder>() {

    inner class ChannelViewHolder(private val binding: RvItemChannelBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(channel: ChannelResponse) {
            val avatarUrl = channel.avatarUrl
            val name = channel.name
            val link = channel.link
            val type = channel.type
            val rating = channel.rating
            if (channel.avatarUrl!="null"){
             Glide.with(context)
                 .load(avatarUrl)
                 .apply(RequestOptions.circleCropTransform())
                 .into(binding.ivAvatar)
            }
            binding.tvName.text = name
            if(link!=null) {
                binding.tvLink.show()
                binding.tvLink.text = "@$link"
            } else   binding.tvLink.hide()

            if (type==ChannelType.PRIVATE) binding.ivClose.show() else binding.ivClose.hide()

            binding.tvRating.text = rating.toString()
            binding.ivRating.colorByRating(rating)
            binding.root.setOnClickListener {
                val switchingToChannelPageActivity =
                    Intent(context, ChannelPageActivity()::class.java)
                switchingToChannelPageActivity.putExtra("CHANNEL_ID", channel.channelId)
                context.startActivity(switchingToChannelPageActivity)
            }
            if (channel.confirmed) binding.ivConfirmed.show() else binding.ivConfirmed.hide()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val binding = RvItemChannelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChannelViewHolder(binding)
    }

    override fun getItemCount(): Int = channelList.size

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.bind(channelList[position])
    }
}