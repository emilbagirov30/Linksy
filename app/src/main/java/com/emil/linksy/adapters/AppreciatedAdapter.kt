package com.emil.linksy.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.PostAppreciatedResponse
import com.emil.linksy.presentation.ui.page.UserPageActivity
import com.emil.linksy.util.colorByRating
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.emil.presentation.databinding.RvItemAppreciatedBinding

class AppreciatedAdapter (private val list: List<PostAppreciatedResponse>, private val context: Context): RecyclerView.Adapter<AppreciatedAdapter.AppreciatedViewHolder>() {

    inner class  AppreciatedViewHolder(private val binding: RvItemAppreciatedBinding) : RecyclerView.ViewHolder(binding.root) {
        private val sharedPref: SharedPreferences = context.getSharedPreferences("appData", Context.MODE_PRIVATE)
        val id = sharedPref.getLong("ID", -1)
        @SuppressLint("SetTextI18n")
        fun bind(user:PostAppreciatedResponse){

            if (user.online) binding.ivOnline.show() else  binding.ivOnline.hide()
            if (user.confirmed) binding.ivConfirmed.show() else binding.ivConfirmed.hide()
            binding.tvUsername.text = user.username
            if (user.link != null) {
                binding.tvLink.show()
                binding.tvLink.text = "@${user.link}"
            }else binding.tvLink.hide()

            binding.root.setOnClickListener {
                if (id != user.id) {
                    val switchingToUserPageActivity = Intent(context, UserPageActivity::class.java)
                    switchingToUserPageActivity.putExtra("USER_ID", user.id)
                    context.startActivity(switchingToUserPageActivity)
                }
            }

            if (user.avatarUrl != "null") {
                Glide.with(context)
                    .load(user.avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivUserAvatar)
            }
            val score = user.score
            binding.tvScore.text = score.toString()
            binding.ivScore.colorByRating(score.toDouble())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppreciatedViewHolder {
        val binding =  RvItemAppreciatedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  AppreciatedViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder:  AppreciatedViewHolder, position: Int) {
        holder.bind (list[position])
    }
}