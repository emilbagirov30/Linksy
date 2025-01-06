package com.emil.linksy.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.PostResponse
import com.emil.domain.model.UserResponse
import com.emil.linksy.presentation.viewmodel.PeopleViewModel
import com.emil.linksy.presentation.viewmodel.PostViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.google.android.material.textview.MaterialTextView

class UsersAdapter(private val userList: List<UserResponse>, private val peopleViewModel: PeopleViewModel,
                   private val context: Context, private val tokenManager: TokenManager
): RecyclerView.Adapter<UsersAdapter.UserViewHolder>()  {



    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatarImageView = itemView.findViewById<ImageView>(R.id.iv_user_avatar)
        private val usernameTextView = itemView.findViewById<MaterialTextView>(R.id.tv_username)
        private val linkTextView = itemView.findViewById<MaterialTextView>(R.id.tv_link)
        private val userLinearLayout = itemView.findViewById<LinearLayout>(R.id.ll_user)
        fun bind(user:UserResponse){
            if (user.avatarUrl !="null"){
                Glide.with(context)
                    .load(user.avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatarImageView)
            }
            usernameTextView.text = user.username
            if (user.link!=null){
                linkTextView.show()
                linkTextView.text = "@${user.link}"
            }
            userLinearLayout.setOnClickListener {

            }
        }


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersAdapter.UserViewHolder, position: Int) {
        holder.bind (userList[position])
    }

    override fun getItemCount(): Int = userList.size



}