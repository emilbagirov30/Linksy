package com.emil.linksy.adapters


import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.ChannelResponse
import com.emil.domain.model.ChannelType
import com.emil.linksy.presentation.ui.page.ChannelPageActivity
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.colorByRating
import com.emil.linksy.util.hide
import com.emil.linksy.util.show


import com.emil.presentation.databinding.RvItemChannelBinding

class ChannelsAdapter(private val channelList: List<ChannelResponse>) : RecyclerView.Adapter<ChannelsAdapter.ChannelViewHolder>() {

    inner class ChannelViewHolder(private val binding: RvItemChannelBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context
        @SuppressLint("SetTextI18n")
        fun bind(channel: ChannelResponse) {
            binding.ivClose.hide()
            binding.ivConfirmed.hide()
            binding.ivClose.hide()
            binding.tvLink.hide()
            val avatarUrl = channel.avatarUrl
            val name = channel.name
            val link = channel.link
            val type = channel.type
            val rating = channel.rating
            binding.tvRating.text = rating.toString()
            binding.ivRating.colorByRating(rating)
            binding.tvName.text = name
            if (channel.avatarUrl!= Linksy.EMPTY_AVATAR){
             Glide.with(context)
                 .load(avatarUrl)
                 .apply(RequestOptions.circleCropTransform())
                 .into(binding.ivAvatar)
            }

            if(link!=null) {
                binding.tvLink.show()
                binding.tvLink.text = "@$link"
            }
            if (type==ChannelType.PRIVATE) binding.ivClose.show()

            binding.root.setOnClickListener {
                val switchingToChannelPageActivity =
                    Intent(context, ChannelPageActivity()::class.java)
                switchingToChannelPageActivity.putExtra(Linksy.INTENT_CHANNEL_ID_KEY, channel.channelId)
                context.startActivity(switchingToChannelPageActivity)
            }
            if (channel.confirmed) binding.ivConfirmed.show()
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