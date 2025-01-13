package com.emil.linksy.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.UserResponse
import com.emil.linksy.presentation.ui.page.UserPageActivity
import com.emil.linksy.presentation.viewmodel.ChannelViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.google.android.material.textview.MaterialTextView

class UsersAdapter(
    private val userList: MutableList<UserResponse>,
    private val context: Context,
    private val isNeedChoice: Boolean = false,
    private val isChannelAdmin: Boolean = false,
    private val channelId:Long? = null,
    private val channelViewModel: ChannelViewModel? = null,
    private val tokenManager: TokenManager? = null
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    private val selectedUserIds = mutableSetOf<Long>()

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatarImageView = itemView.findViewById<ImageView>(R.id.iv_user_avatar)
        private val usernameTextView = itemView.findViewById<MaterialTextView>(R.id.tv_username)
        private val linkTextView = itemView.findViewById<MaterialTextView>(R.id.tv_link)
        private val userLinearLayout = itemView.findViewById<LinearLayout>(R.id.ll_user)
        private val selectorCheckBox = itemView.findViewById<CheckBox>(R.id.сb_selector)
        private val acceptImageButton = itemView.findViewById<ImageButton>(R.id.ib_accept)
        private val rejectImageButton = itemView.findViewById<ImageButton>(R.id.ib_reject)
        private val sharedPref: SharedPreferences = context.getSharedPreferences("AppData", Context.MODE_PRIVATE)
        val id = sharedPref.getLong("ID", -1)

        @SuppressLint("SetTextI18n")
        fun bind(user: UserResponse) {
            if (isNeedChoice) {
                selectorCheckBox.show()
                selectorCheckBox.isChecked = selectedUserIds.contains(user.id)

                selectorCheckBox.setOnClickListener {
                    toggleSelection(user.id)
                }

                userLinearLayout.setOnClickListener {
                    selectorCheckBox.isChecked = !selectorCheckBox.isChecked
                    toggleSelection(user.id)
                }
            } else {
                userLinearLayout.setOnClickListener {
                    if (id != user.id) {
                        val switchingToUserPageActivity = Intent(context, UserPageActivity::class.java)
                        switchingToUserPageActivity.putExtra("USER_ID", user.id)
                        context.startActivity(switchingToUserPageActivity)
                    }
                }
            }


            if (isChannelAdmin){
                rejectImageButton.show()
                acceptImageButton.show()

                rejectImageButton.setOnClickListener {
                    it.anim()
                    channelViewModel?.rejectSubscriptionRequest(tokenManager!!.getAccessToken(),channelId!!,user.id, onConflict = {}, onSuccess = {
                        userList.removeAt(bindingAdapterPosition)
                        notifyItemRemoved(bindingAdapterPosition)
                    })
                }

                acceptImageButton.setOnClickListener {
                    it.anim()
                    channelViewModel?.acceptUserToChannel(tokenManager!!.getAccessToken(),channelId!!,user.id, onConflict = {}, onSuccess = {
                        userList.removeAt(bindingAdapterPosition)
                        notifyItemRemoved(bindingAdapterPosition)
                    })
                }
            }

            if (user.avatarUrl != "null") {
                Glide.with(context)
                    .load(user.avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatarImageView)
            }

            usernameTextView.text = user.username
            if (user.link != null) {
                linkTextView.show()
                linkTextView.text = "@${user.link}"
            }
        }

        private fun toggleSelection(userId: Long) {
            if (selectedUserIds.contains(userId)) {
                selectedUserIds.remove(userId)
            } else {
                selectedUserIds.add(userId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersAdapter.UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int = userList.size

    fun getSelectedUserIds(): List<Long> {
        return selectedUserIds.toList()
    }
}
