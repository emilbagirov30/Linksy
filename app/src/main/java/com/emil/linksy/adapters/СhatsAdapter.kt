package com.emil.linksy.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.ChatResponse
import com.emil.linksy.presentation.ui.navigation.chat.MessageActivity
import com.emil.linksy.presentation.viewmodel.ChatViewModel
import com.emil.presentation.R
import com.google.android.material.textview.MaterialTextView

class ChatsAdapter(private val chatList: List<ChatResponse>,
                   private val context: Context
): RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val chatLayout = itemView.findViewById<LinearLayout>(R.id.ll_chat)
        private val avatarImageView = itemView.findViewById<ImageView>(R.id.iv_avatar)
        private val nameTextView = itemView.findViewById<MaterialTextView>(R.id.tv_name)
        private val dateTextView = itemView.findViewById<TextView>(R.id.tv_date)
        private val lastMessageTextView = itemView.findViewById<MaterialTextView>(R.id.tv_last_message)

      fun bind(chat:ChatResponse){
          val id = chat.chatId
          val userId = chat.companionId
          val isGroup = chat.isGroup
          val avatarUrl = chat.avatarUrl
          val name = chat.displayName
          val lastMessage = chat.lastMessage
          val date = chat.dateLast
          if (avatarUrl !="null"){
              Glide.with(context)
                  .load(avatarUrl)
                  .apply(RequestOptions.circleCropTransform())
                  .into(avatarImageView)
          }else{
              if (isGroup)
                  avatarImageView.setBackgroundResource(R.drawable.default_group_avatar)

          }



          nameTextView.text = name
          dateTextView.text = date
          lastMessageTextView.text = lastMessage

          chatLayout.setOnClickListener {
              val startMessageActivity = Intent(context, MessageActivity::class.java)
              startMessageActivity.putExtra("USER_ID",userId)
              startMessageActivity.putExtra("CHAT_ID",id)
              startMessageActivity.putExtra("ISGROUP",isGroup)
              startMessageActivity.putExtra("AVATAR_URL",avatarUrl)
              startMessageActivity.putExtra("NAME",name)
              context.startActivity(startMessageActivity)
          }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int = chatList.size


    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind (chatList[position])
    }
}