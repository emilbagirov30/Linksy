package com.emil.linksy.adapters

import android.annotation.SuppressLint
import android.content.Context
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

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.PostResponse
import com.emil.linksy.presentation.ui.ActionDialog
import com.emil.linksy.presentation.ui.BigPictureDialog
import com.emil.linksy.presentation.ui.VideoPlayerDialog
import com.emil.linksy.presentation.viewmodel.PostViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.show
import com.emil.linksy.util.showMenu
import com.emil.presentation.R
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PostsAdapter(private val postList: List<PostResponse>, private val postViewModel: PostViewModel,
                   private val context:Context, private val tokenManager: TokenManager? =null): RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    inner class PostsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val authorAvatarImageView = itemView.findViewById<ImageView>(R.id.iv_author_avatar)
        private val postPictureImageView = itemView.findViewById<ImageView>(R.id.iv_post_image)
        private val authorUsername = itemView.findViewById<MaterialTextView>(R.id.tv_author_username)
        private val publicationDate = itemView.findViewById<MaterialTextView>(R.id.tv_date)
        private val postTextView = itemView.findViewById<MaterialTextView>(R.id.tv_text_post_content)
        private val likeCount = itemView.findViewById<MaterialTextView>(R.id.tv_like_count)
        private val repostsCount = itemView.findViewById<MaterialTextView>(R.id.tv_reposts_count)
        private val editPostButton = itemView.findViewById<ImageButton>(R.id.ib_edit_post)
        private val videoRelativeLayout = itemView.findViewById<RelativeLayout>(R.id.rl_post_video)
        private val mediaLinearLayout = itemView.findViewById<LinearLayout>(R.id.ll_media)
        private val frameImageView = itemView.findViewById<ImageView>(R.id.iv_frame)
        private val playVideoImageButton = itemView.findViewById<ImageButton>(R.id.ib_play)
        private val audioLinearLayout = itemView.findViewById<LinearLayout>(R.id.ll_audio)
        private val voiceLinearLayout = itemView.findViewById<LinearLayout>(R.id.ll_voice)
        private val playAudioButton = itemView.findViewById<ImageView>(R.id.iv_play_audio)
        private val playVoiceButton = itemView.findViewById<ImageView>(R.id.iv_play_voice)
        private val audioProgressBar = itemView.findViewById<ProgressBar>(R.id.pb_audio)
        private val voiceProgressBar = itemView.findViewById<ProgressBar>(R.id.pb_voice)
        @SuppressLint("SetTextI18n")
        fun bind(post: PostResponse) {
              if (post.authorAvatarUrl !="null"){
                  Glide.with(context)
                      .load(post.authorAvatarUrl)
                      .apply(RequestOptions.circleCropTransform())
                      .into(authorAvatarImageView)
              }
            authorUsername.text = post.authorUsername
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
            var mediaPlayerAudio:MediaPlayer? = null
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



            var isPlayingVoice = false
            var mediaPlayerVoice:MediaPlayer? = null
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
            if (post.voiceUrl!=null) {
                voiceLinearLayout.show()
                playVoiceButton.setImageResource(R.drawable.ic_play)
                mediaPlayerVoice = MediaPlayer().apply {
                    setDataSource(context, Uri.parse(post.voiceUrl))
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

            likeCount.text = post.likesCount.toString()
            repostsCount.text = post.repostsCount.toString()
            editPostButton.setOnClickListener {
                if (tokenManager!=null) {
                    it.showMenu(context, editAction = {}, deleteAction = {
                        val dialog = ActionDialog(context)
                        dialog.setTitle(context.getString(R.string.delete_post_title))
                        dialog.setConfirmText(context.getString(R.string.delete_post_confirm_text))
                        dialog.setAction {
                            val token = tokenManager.getAccessToken()
                            postViewModel.deletePost(token, post.postId, onSuccess = { dialog.dismiss() }, onError = {})
                        }

                    })
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_posts, parent, false)
        return PostsViewHolder(view)
    }

    override fun getItemCount(): Int = postList.size


    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        holder.bind (postList[position])
    }

}