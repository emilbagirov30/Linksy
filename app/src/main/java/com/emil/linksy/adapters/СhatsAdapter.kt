package com.emil.linksy.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.ChatResponse
import com.emil.linksy.presentation.ui.ActionDialog
import com.emil.linksy.presentation.ui.navigation.chat.ChatFragment
import com.emil.linksy.presentation.ui.navigation.chat.MessageActivity
import com.emil.linksy.presentation.viewmodel.ChatViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.showMenu
import com.emil.presentation.R
import com.google.android.material.textview.MaterialTextView

class ChatsAdapter(private val chatList: List<ChatResponse>,
                   private val context: Context,
                   private val chatViewModel: ChatViewModel,
                   private val tokenManager: TokenManager,
                   private val chatFragment: ChatFragment
): RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val chatLayout = itemView.findViewById<LinearLayout>(R.id.ll_chat)
        private val countLayout = itemView.findViewById<FrameLayout>(R.id.fl_count)
        private val countTextView = itemView.findViewById<MaterialTextView>(R.id.tv_count)
        private val avatarImageView = itemView.findViewById<ImageView>(R.id.iv_avatar)
        private val nameTextView = itemView.findViewById<MaterialTextView>(R.id.tv_name)
        private val dateTextView = itemView.findViewById<TextView>(R.id.tv_date)
        private val lastMessageTextView = itemView.findViewById<MaterialTextView>(R.id.tv_last_message)
        private val confirmedImageView = itemView.findViewById<ImageView>(R.id.iv_confirmed)
      fun bind(chat:ChatResponse){
          val id = chat.chatId
          val userId = chat.companionId
          val isGroup = chat.isGroup
          val avatarUrl = chat.avatarUrl
          val name = chat.displayName
          val lastMessage = chat.lastMessage
          val date = chat.dateLast
          if (chat.confirmed) confirmedImageView.show() else confirmedImageView.hide()

          if (avatarUrl !="null"){
              Glide.with(context)
                  .load(avatarUrl)
                  .apply(RequestOptions.circleCropTransform())
                  .into(avatarImageView)
          } else {
              if (isGroup) avatarImageView.setBackgroundResource(R.drawable.default_group_avatar)
                   else avatarImageView.setBackgroundResource(R.drawable.default_avatar)
          }

          if (chat.unreadMessagesCount!=null && chat.unreadMessagesCount!!>0){
              countLayout.show()
              countTextView.text = chat.unreadMessagesCount.toString()
          }else countLayout.hide()

          nameTextView.text = name
          if (lastMessage.isNotEmpty() || date.isEmpty()) lastMessageTextView.text = lastMessage else {
              lastMessageTextView.setTextColor(ContextCompat.getColor(context,R.color.green))
              lastMessageTextView.text = context.getString(R.string.attachment)
          }
          dateTextView.text = date.ifEmpty { context.getString(R.string.new_) }
          chatLayout.setOnClickListener {
              val startMessageActivity = Intent(context, MessageActivity::class.java)
              startMessageActivity.putExtra("USER_ID",userId)
              startMessageActivity.putExtra("CHAT_ID",id)
              startMessageActivity.putExtra("ISGROUP",isGroup)
              startMessageActivity.putExtra("CONFIRMED",chat.confirmed)
              startMessageActivity.putExtra("AVATAR_URL",avatarUrl)
              startMessageActivity.putExtra("NAME",name)
              context.startActivity(startMessageActivity)
          }

          chatLayout.setOnLongClickListener {
              it.showMenu(context,
                  deleteAction = {
                      val dialog = ActionDialog(context)
                      dialog.setTitle(context.getString(R.string.delete_chat_title))
                      dialog.setConfirmText(context.getString(R.string.delete_chat_confirm_text))
                      dialog.setAction {
                          chatViewModel.deleteChat(tokenManager.getAccessToken(),chat.chatId, onSuccess = {
                              chatFragment.getUserChats()
                              dialog.dismiss()
                          })
                      }
                  }
              )

              true
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