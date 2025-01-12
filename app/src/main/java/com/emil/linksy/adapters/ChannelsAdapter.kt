package com.emil.linksy.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.ChannelResponse
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
            link?.let {
                binding.tvLink.show()
                binding.tvLink.text = "@$link"
            }

            if (type=="PRIVATE") binding.ivClose.show()

             if (rating<0) binding.tvRating.text = "-"
             else binding.tvRating.text = rating.toString()

            if (rating<2.99 && rating>0)
                ViewCompat.setBackgroundTintList(binding.ivRating, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red)))
            if(rating >= 3.0 && rating < 4.0)  ViewCompat.setBackgroundTintList(binding.ivRating, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.yellow)))
            if(rating >=4)  ViewCompat.setBackgroundTintList(binding.ivRating, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green)))

            binding.root.setOnClickListener {

            }
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