package com.emil.linksy.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.PostAppreciatedResponse
import com.emil.linksy.presentation.ui.page.UserPageActivity
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.colorByRating
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.emil.presentation.databinding.RvItemAppreciatedBinding

class AppreciatedAdapter: ListAdapter<PostAppreciatedResponse, AppreciatedAdapter.AppreciatedViewHolder>(PostAppreciatedDiffCallback ()) {

    inner class  AppreciatedViewHolder(private val binding: RvItemAppreciatedBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context
        private val sharedPref: SharedPreferences = context.getSharedPreferences(Linksy.SHAREDPREF_MAIN_KEY, Context.MODE_PRIVATE)
        val id = sharedPref.getLong(Linksy.SHAREDPREF_ID_KEY, Linksy.DEFAULT_ID)

        @SuppressLint("SetTextI18n")
        fun bind(user:PostAppreciatedResponse) {
            binding.ivOnline.hide()
            binding.ivConfirmed.hide()
            binding.tvLink.hide()
            binding.ivUserAvatar.setBackgroundResource(R.drawable.default_avatar)
            if (user.online) binding.ivOnline.show()
            if (user.confirmed) binding.ivConfirmed.show()
            if (user.link != null) {
                binding.tvLink.show()
                binding.tvLink.text = "@${user.link}"
            }
            binding.root.setOnClickListener {
                if (id != user.id) {
                    val switchingToUserPageActivity = Intent(context, UserPageActivity::class.java)
                    switchingToUserPageActivity.putExtra(Linksy.INTENT_USER_ID_KEY, user.id)
                    context.startActivity(switchingToUserPageActivity)
                }
            }
            if (user.avatarUrl != Linksy.EMPTY_AVATAR) {
                Glide.with(context)
                    .load(user.avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivUserAvatar)
            }
            binding.tvUsername.text = user.username
            val score = user.score
            binding.tvScore.text = score.toString()
            binding.ivScore.colorByRating(score.toDouble())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppreciatedViewHolder {
        val binding =  RvItemAppreciatedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  AppreciatedViewHolder(binding)
    }

    override fun onBindViewHolder(holder:  AppreciatedViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    @SuppressLint("DiffUtilEquals")
    class PostAppreciatedDiffCallback : DiffUtil.ItemCallback<PostAppreciatedResponse>() {
        override fun areItemsTheSame(oldItem: PostAppreciatedResponse, newItem: PostAppreciatedResponse): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: PostAppreciatedResponse, newItem: PostAppreciatedResponse): Boolean = oldItem == newItem
    }
}