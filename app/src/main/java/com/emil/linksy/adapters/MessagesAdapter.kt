package com.emil.linksy.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.MessageResponse
import com.emil.domain.model.UserResponse
import com.emil.linksy.presentation.ui.ActionDialog
import com.emil.linksy.presentation.ui.BigPictureDialog
import com.emil.linksy.presentation.ui.VideoPlayerDialogFragment
import com.emil.linksy.presentation.ui.navigation.chat.EditMessageDialog
import com.emil.linksy.presentation.ui.page.UserPageActivity
import com.emil.linksy.presentation.viewmodel.MessageViewModel
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.emil.presentation.databinding.RvItemMessageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MessagesAdapter(private val messageList: List<MessageResponse>,
                      private val userId: Long,
                      private val chatSendersList:List<UserResponse> = emptyList(),
                      private val messageViewModel: MessageViewModel,
                      private val tokenManager: TokenManager
):RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>(){

    inner class MessageViewHolder(private val binding:RvItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context
        private val container = binding.container
        private val mainHorizontalLayout = binding.flMain
        private val mainLayout = binding.llMain
        private val messageLayout = binding.llMessage
        private val cardView = binding.cwMain
        private val pictureImageView = binding.ivPicture
        private val senderAvatarImageView = binding.ivSenderAvatar
        private val senderUsernameTextView = binding.tvSenderUsername
        private val videoRelativeLayout = binding.rlVideo
        private val frameImageView = binding.ivFrame
        private val markImageView = binding.ivViewedMark
        private val playVideoButton = binding.ibPlay
        private val playAudioButton = binding.ivPlayAudio
        private val playVoiceButton = binding.ivPlayVoice
        private val audioProgressBar = binding.pbPickedAudio
        private val voiceProgressBar = binding.pbVoice
        private val audioLayout = binding.llAudio
        private val voiceLayout = binding.llVoice
        private val messageTextView = binding.tvMessage
        private val timeTextView = binding.tvTime
        private val editedTextView = binding.tvEdited


        @SuppressLint("RtlHardcoded")
        fun bind(message: MessageResponse) {
            if (userId == message.senderId) {
                mainLayout.layoutDirection = LinearLayout.LAYOUT_DIRECTION_RTL
                messageLayout.setBackgroundResource(R.drawable.user_message)
                val params = container.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.RIGHT
                cardView.layoutParams = params
                markImageView.show()
                if (message.viewed) markImageView.setBackgroundResource(R.drawable.ic_viewed)
                else markImageView.setBackgroundResource(R.drawable.ic_not_read)
            } else {
                markImageView.hide()
                mainLayout.layoutDirection = LinearLayout.LAYOUT_DIRECTION_LTR
                messageLayout.setBackgroundResource(R.drawable.stranger_message)
                val params = container.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.LEFT
                cardView.layoutParams = params
            }
            val text = message.text
            val imageUrl = message.imageUrl
            val videoUrl = message.videoUrl
            val audioUrl = message.audioUrl
            val voiceUrl = message.voiceUrl
            val date = message.date
            var isPlayingAudio = false
            var mediaPlayerAudio: MediaPlayer? = null
            var isPlayingVoice = false
            var mediaPlayerVoice: MediaPlayer? = null
            messageTextView.hide()
            pictureImageView.hide()
            videoRelativeLayout.hide()
            voiceLayout.hide()
            audioLayout.hide()
            editedTextView.hide()
            senderAvatarImageView.hide()
            senderUsernameTextView.hide()
            timeTextView.text = date

            if (text != null) {
                messageTextView.show()
                messageTextView.text = message.text
            }

           if (imageUrl != null) {
                pictureImageView.show()
                Glide.with(context).load(Uri.parse(imageUrl)).into(pictureImageView)
               pictureImageView.setOnClickListener {
                   BigPictureDialog.newInstance(imageUrl).show((context as AppCompatActivity).supportFragmentManager, "BigPictureDialog")
               }
            }

          if (videoUrl != null) {
                videoRelativeLayout.show()
                Glide.with(context).load(Uri.parse(videoUrl)).frame(0).into(frameImageView)
                playVideoButton.setOnClickListener {
                    val playerDialog = VideoPlayerDialogFragment.newInstance(url = videoUrl)
                    playerDialog.show((context as AppCompatActivity).supportFragmentManager, "VideoPlayerDialogFragment")

                }
            }


            fun startProgressAudioUpdate() {
                CoroutineScope(Dispatchers.Main).launch {
                    while (mediaPlayerAudio!!.isPlaying) {
                        val currentPosition = mediaPlayerAudio!!.currentPosition
                        val totalDuration = mediaPlayerAudio!!.duration
                        audioProgressBar.max = totalDuration
                        audioProgressBar.progress = currentPosition
                        delay(5)
                    }
                }
            }

            if (audioUrl!=null){
                audioLayout.show()
                playAudioButton.setImageResource(R.drawable.ic_play)
                mediaPlayerAudio = MediaPlayer().apply {
                    setDataSource(context, Uri.parse(audioUrl))
                    prepareAsync()
                    setOnPreparedListener {
                        audioProgressBar.progress = 0
                        audioProgressBar.max = it.duration
                        isPlayingAudio = false
                    }
                    setOnCompletionListener {
                        audioProgressBar.progress = 0
                        isPlayingAudio = false
                        playAudioButton.setImageResource(R.drawable.ic_play)
                    }
                    playAudioButton.setOnClickListener {
                        if (isPlayingAudio) {
                            mediaPlayerAudio?.pause()
                            playAudioButton.setImageResource(R.drawable.ic_play)
                            isPlayingAudio = false
                        } else {
                            mediaPlayerAudio?.start()
                            playAudioButton.setImageResource(R.drawable.ic_pause)
                            isPlayingAudio = true
                            startProgressAudioUpdate()

                        }
                    }

                }
            }


            fun startProgressVoiceUpdate() {
                CoroutineScope(Dispatchers.Main).launch {
                    while (mediaPlayerVoice!!.isPlaying) {
                        val currentPosition = mediaPlayerVoice!!.currentPosition
                        val totalDuration = mediaPlayerVoice!!.duration
                        voiceProgressBar.max = totalDuration
                        voiceProgressBar.progress = currentPosition
                        delay(5)
                    }
                }
            }

          if (voiceUrl!=null) {
                voiceLayout.show()
                playVoiceButton.setImageResource(R.drawable.ic_play)

                mediaPlayerVoice = MediaPlayer().apply {
                    setDataSource(context, Uri.parse(voiceUrl))
                    prepareAsync()
                    setOnPreparedListener {
                        voiceProgressBar.progress = 0
                        voiceProgressBar.max = it.duration
                        isPlayingVoice = false
                    }


                    setOnCompletionListener {
                        voiceProgressBar.progress = 0
                        isPlayingVoice = false
                        playVoiceButton.setImageResource(R.drawable.ic_play)
                    }


                    playVoiceButton.setOnClickListener {
                        if (isPlayingVoice) {
                            mediaPlayerVoice?.pause()
                            playVoiceButton.setImageResource(R.drawable.ic_play)
                            isPlayingVoice = false
                        } else {
                            mediaPlayerVoice?.start()
                            playVoiceButton.setImageResource(R.drawable.ic_pause)
                            isPlayingVoice = true
                            startProgressVoiceUpdate()
                        }
                    }

                }

            }

            if (message.edited) editedTextView.show()

    mainHorizontalLayout.setOnClickListener {
    if(message.senderId==userId)
    showPopup(it,message.messageId,if (message.text==null) "" else message.text!!)
    }


if (chatSendersList.isNotEmpty()){
    senderAvatarImageView.show()
    senderUsernameTextView.show()
    val senderId = message.senderId
    val sender = chatSendersList.find { it.id == senderId }
    val senderAvatarUrl = sender?.avatarUrl
    val senderUsername = sender?.username
    senderUsernameTextView.text = senderUsername
    if (senderAvatarUrl!= Linksy.EMPTY_AVATAR){
        Glide.with(context)
            .load(senderAvatarUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(senderAvatarImageView)
    } else senderAvatarImageView.setBackgroundResource(R.drawable.default_avatar)
    senderAvatarImageView.setOnClickListener {
        if (senderId != userId) {
            val switchingToUserPageActivity = Intent(context, UserPageActivity::class.java)
            switchingToUserPageActivity.putExtra(Linksy.INTENT_USER_ID_KEY, senderId)
            context.startActivity(switchingToUserPageActivity)
        }
    }
}
        }
    }

    @SuppressLint("InflateParams")
    private fun showPopup(anchor: View, messageId: Long, text: String) {
        val context = anchor.context
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_message, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popupWidth = popupView.measuredWidth
        val popupHeight = popupView.measuredHeight


        val location = IntArray(2)
        anchor.getLocationOnScreen(location)
        val anchorX = location[0] + anchor.width / 2
        val anchorY = location[1] + anchor.height / 2


        val xOffset = anchorX - popupWidth / 2
        val yOffset = anchorY - popupHeight / 2


        popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xOffset, yOffset)

        val edit = popupView.findViewById<TextView>(R.id.tv_edit)
        val delete = popupView.findViewById<TextView>(R.id.tv_delete)

        edit.setOnClickListener {
            EditMessageDialog.newInstance(context, messageId, text, tokenManager, messageViewModel)
            popupWindow.dismiss()
        }
        delete.setOnClickListener {
            val dialog = ActionDialog(context)
            dialog.setTitle(context.getString(R.string.delete_message_title))
            dialog.setConfirmText(context.getString(R.string.delete_message_confirm_text))
            dialog.setAction {
                messageViewModel.deleteMessage(tokenManager.getAccessToken(), messageId =messageId, onSuccess = {
                    dialog.dismiss()
                    popupWindow.dismiss()
                } )

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = RvItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }
    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind (messageList[position])
    }
}