package com.emil.linksy.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.MessageResponse
import com.emil.domain.model.UserResponse
import com.emil.linksy.presentation.ui.BigPictureDialog
import com.emil.linksy.presentation.ui.VideoPlayerDialog
import com.emil.linksy.util.show
import com.emil.presentation.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MessagesAdapter(private val messageList: List<MessageResponse>,
                      private val context: Context,
                      private val userId: Long,
                      private val chatMemberList:List<UserResponse> = emptyList()
):RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>(){

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val container = itemView.findViewById<FrameLayout>(R.id.container)
        private val mainLayout = itemView.findViewById<LinearLayout>(R.id.ll_main)
        private val messageLayout = itemView.findViewById<LinearLayout>(R.id.ll_message)
        private val cardView = itemView.findViewById<CardView>(R.id.cw_main)
        private val pictureImageView = itemView.findViewById<ImageView>(R.id.iv_picture)
        private val senderAvatarImageView = itemView.findViewById<ImageView>(R.id.iv_sender_avatar)
        private val senderUsernameTextView = itemView.findViewById<TextView>(R.id.tv_sender_username)
        private val videoRelativeLayout = itemView.findViewById<RelativeLayout>(R.id.rl_video)
        private val frameImageView = itemView.findViewById<ImageView>(R.id.iv_frame)
        private val playVideoButton = itemView.findViewById<ImageButton>(R.id.ib_play)
        private val playAudioButton = itemView.findViewById<ImageView>(R.id.iv_play_audio)
        private val playVoiceButton = itemView.findViewById<ImageView>(R.id.iv_play_voice)
        private val audioProgressBar = itemView.findViewById<ProgressBar>(R.id.pb_audio)
        private val voiceProgressBar = itemView.findViewById<ProgressBar>(R.id.pb_voice)
        private val audioLayout = itemView.findViewById<LinearLayout>(R.id.ll_audio)
        private val voiceLayout = itemView.findViewById<LinearLayout>(R.id.ll_voice)
        private val messageTextView = itemView.findViewById<TextView>(R.id.tv_message)
        private val timeTextView = itemView.findViewById<TextView>(R.id.tv_time)

        @SuppressLint("RtlHardcoded")
        fun bind(message: MessageResponse) {
            if (userId == message.senderId) {
                mainLayout.layoutDirection = LinearLayout.LAYOUT_DIRECTION_RTL
                messageLayout.setBackgroundResource(R.drawable.user_message)
                val params = container.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.RIGHT
                cardView.layoutParams = params
            } else {
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
            text?.let {
                messageTextView.show()
                messageTextView.text = it
            }
            imageUrl?.let {
                pictureImageView.show()
                Glide.with(context).load(Uri.parse(it)).into(pictureImageView)
               pictureImageView.setOnClickListener {
                   BigPictureDialog(context, imageUrl).show((context as AppCompatActivity).supportFragmentManager, "BigPictureDialog")
               }
            }
            videoUrl?.let {
                videoRelativeLayout.show()
                Glide.with(context).load(Uri.parse(it)).frame(0).into(frameImageView)
                playVideoButton.setOnClickListener { VideoPlayerDialog(context, videoUrl) }
            }
            timeTextView.text = date


            var isPlayingAudio = false
            var mediaPlayerAudio: MediaPlayer? = null
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

            audioUrl?.let {
                audioLayout.show()
                playAudioButton.setImageResource(R.drawable.ic_play)
                mediaPlayerAudio = MediaPlayer().apply {
                    setDataSource(context, Uri.parse(it))
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

            var isPlayingVoice = false
            var mediaPlayerVoice: MediaPlayer? = null
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
            voiceUrl?.let {
                voiceLayout.show()
                playVoiceButton.setImageResource(R.drawable.ic_play)
                mediaPlayerVoice = MediaPlayer().apply {
                    setDataSource(context, Uri.parse(it))
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

if (chatMemberList.isNotEmpty()){
    senderAvatarImageView.show()
    senderUsernameTextView.show()
   val senderId = message.senderId
   val sender = chatMemberList.find { it.id == senderId }
    val senderAvatarUrl =sender?.avatarUrl
    val senderUsername = sender?.username
    senderUsernameTextView.text = senderUsername
    if (senderAvatarUrl!="null"){
        Glide.with(context)
            .load(senderAvatarUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(senderAvatarImageView)
    }
}
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_message, parent, false)
        return MessageViewHolder(view)

    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind (messageList[position])
    }
}