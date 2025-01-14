package com.emil.linksy.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.ChannelPostResponse
import com.emil.linksy.presentation.ui.ActionDialog
import com.emil.linksy.presentation.ui.BigPictureDialog
import com.emil.linksy.presentation.ui.VideoPlayerDialog
import com.emil.linksy.presentation.viewmodel.ChannelViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.show
import com.emil.linksy.util.showMenu
import com.emil.presentation.R
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChannelPostsAdapter(private val postlist: List<ChannelPostResponse>,private val context: Context,private val tokenManager: TokenManager,private val channelViewModel: ChannelViewModel
): RecyclerView.Adapter<ChannelPostsAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val channelAvatarImageView = itemView.findViewById<ImageView>(R.id.iv_channel_avatar)
        private val postPictureImageView = itemView.findViewById<ImageView>(R.id.iv_post_image)
        private val channelName = itemView.findViewById<MaterialTextView>(R.id.tv_channel_name)
        private val publicationDate = itemView.findViewById<MaterialTextView>(R.id.tv_date)
        private val postTextView = itemView.findViewById<MaterialTextView>(R.id.tv_text_post_content)
        private val ratingTextView = itemView.findViewById<MaterialTextView>(R.id.tv_rating_average)
        private val ratingImageView = itemView.findViewById<ImageView>(R.id.iv_rating)
        private val repostsCount = itemView.findViewById<MaterialTextView>(R.id.tv_reposts_count)
        private val editPostButton = itemView.findViewById<ImageButton>(R.id.ib_edit_post)
        private val videoRelativeLayout = itemView.findViewById<RelativeLayout>(R.id.rl_post_video)
        private val mediaLinearLayout = itemView.findViewById<LinearLayout>(R.id.ll_media)
        private val frameImageView = itemView.findViewById<ImageView>(R.id.iv_frame)
        private val playVideoImageButton = itemView.findViewById<ImageButton>(R.id.ib_play)
        private val audioProgressBar = itemView.findViewById<ProgressBar>(R.id.pb_audio)
        private val audioLinearLayout = itemView.findViewById<LinearLayout>(R.id.ll_audio)
        private val pollTitleTextView = itemView.findViewById<MaterialTextView>(R.id.tv_title)
        private val pollLinearLayout = itemView.findViewById<LinearLayout>(R.id.ll_poll)
        private val playAudioButton = itemView.findViewById<ImageView>(R.id.iv_play_audio)
        private val optionRecyclerView = itemView.findViewById<RecyclerView>(R.id.rv_options)
        @SuppressLint("SuspiciousIndentation", "SetTextI18n")
        fun bind(post:ChannelPostResponse){
            if (post.channelAvatarUrl !="null"){
                Glide.with(context)
                    .load(post.channelAvatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(channelAvatarImageView)
            }
           channelName.text = post.channelName
            publicationDate.text = post.publishDate
            if (post.text!=null) {
                postTextView.show()
                postTextView.text = post.text
            }
            val imageUrl = post.imageUrl
            if (imageUrl !=null){
                mediaLinearLayout.show()
                postPictureImageView.show()
                Glide.with(context)
                    .load(imageUrl)
                    .into(postPictureImageView)

                postPictureImageView.setOnClickListener {
                    BigPictureDialog(context,imageUrl).show((context as AppCompatActivity).supportFragmentManager,  "BigPictureDialog")
                }
            }
            val videoUrl = post.videoUrl
            if (videoUrl!=null){
                mediaLinearLayout.show()
                videoRelativeLayout.show()
                Glide.with(context)
                    .load(Uri.parse(videoUrl))
                    .frame(0)
                    .into(frameImageView)
                playVideoImageButton.setOnClickListener{
                    VideoPlayerDialog(context,videoUrl)
                }

            }
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
            if (post.audioUrl!=null) {
                audioLinearLayout.show()
                playAudioButton.setImageResource(R.drawable.ic_play)
                mediaPlayerAudio = MediaPlayer().apply {
                    setDataSource(context, Uri.parse(post.audioUrl))
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

                   var rating = post.averageRating
                ratingTextView.text = rating.toString()
            if (rating<2.99 && rating>0)
                ViewCompat.setBackgroundTintList(ratingImageView, ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.red)))
            if(rating >= 3.0 && rating < 4.0)  ViewCompat.setBackgroundTintList(ratingImageView, ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.yellow)))
            if(rating >=4)  ViewCompat.setBackgroundTintList(ratingImageView, ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.green)))

            repostsCount.text = post.repostsCount.toString()

                  if (post.options!=null){
                      pollLinearLayout.show()
                      pollTitleTextView.text = post.pollTitle
                      optionRecyclerView.layoutManager = LinearLayoutManager(context)
                      optionRecyclerView.adapter = OptionsAdapter(post.isVoted,post.options!!,channelViewModel,tokenManager)
                  }
            editPostButton.setOnClickListener {
                    it.showMenu(context, editAction = {}, deleteAction = {
                        val dialog = ActionDialog(context)
                        dialog.setTitle(context.getString(R.string.delete_post_title))
                        dialog.setConfirmText(context.getString(R.string.delete_post_confirm_text))
                        dialog.setAction {
                            val token = tokenManager.getAccessToken()
                            channelViewModel.deleteChannelPost(token, post.postId, onSuccess = { dialog.dismiss()}, onConflict = {})
                        }

                    })

            }




        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_channel_posts, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int = postlist.size


    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind (postlist[position])
    }
}