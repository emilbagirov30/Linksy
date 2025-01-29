package com.emil.linksy.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.PostResponse
import com.emil.linksy.presentation.ui.ActionDialog
import com.emil.linksy.presentation.ui.BigPictureDialog
import com.emil.linksy.presentation.ui.VideoPlayerDialog
import com.emil.linksy.presentation.ui.auth.ConfirmCodeBottomSheet
import com.emil.linksy.presentation.ui.navigation.feed.SubscriptionsPostsFeedFragment
import com.emil.linksy.presentation.ui.navigation.people.RelationsDialogFragment
import com.emil.linksy.presentation.ui.navigation.profile.AddPostDialogFragment
import com.emil.linksy.presentation.ui.navigation.profile.CommentsBottomSheet
import com.emil.linksy.presentation.ui.navigation.profile.PostFragment
import com.emil.linksy.presentation.ui.page.OutsiderPostFragment
import com.emil.linksy.presentation.ui.page.UserPageActivity
import com.emil.linksy.presentation.viewmodel.PostViewModel
import com.emil.linksy.util.RelationType
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.showMenu
import com.emil.presentation.R
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PostsAdapter(private val postList: List<PostResponse>, private val postViewModel: PostViewModel,
                   private val context:Context, private val tokenManager: TokenManager, private val postFragment: PostFragment?=null,
                   private val outsiderPostFragment: OutsiderPostFragment?=null,private val subscriptionsPostsFeedFragment: SubscriptionsPostsFeedFragment? = null
    ): RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    inner class PostsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val authorAvatarImageView = itemView.findViewById<ImageView>(R.id.iv_author_avatar)
        private val postPictureImageView = itemView.findViewById<ImageView>(R.id.iv_post_image)
        private val authorUsername = itemView.findViewById<MaterialTextView>(R.id.tv_author_username)
        private val publicationDate = itemView.findViewById<MaterialTextView>(R.id.tv_date)
        private val postTextView = itemView.findViewById<MaterialTextView>(R.id.tv_text_post_content)
        private val likeCount = itemView.findViewById<MaterialTextView>(R.id.tv_like_count)
        private val likeImageView = itemView.findViewById<ImageView>(R.id.iv_like)
        private val commentImageView = itemView.findViewById<ImageView>(R.id.iv_comment)
        private val commentCount = itemView.findViewById<MaterialTextView>(R.id.tv_comment_count)
        private val editPostButton = itemView.findViewById<ImageButton>(R.id.ib_edit_post)
        private val videoRelativeLayout = itemView.findViewById<RelativeLayout>(R.id.rl_post_video)
        private val mediaLinearLayout = itemView.findViewById<LinearLayout>(R.id.ll_media)
        private val frameImageView = itemView.findViewById<ImageView>(R.id.iv_frame)
        private val playVideoImageButton = itemView.findViewById<ImageButton>(R.id.ib_play)
        private val audioLinearLayout = itemView.findViewById<LinearLayout>(R.id.ll_audio)
        private val voiceLinearLayout = itemView.findViewById<LinearLayout>(R.id.ll_voice)
        private val confirmedImageView = itemView.findViewById<ImageView>(R.id.iv_confirmed)
        private val playAudioButton = itemView.findViewById<ImageView>(R.id.iv_play_audio)
        private val playVoiceButton = itemView.findViewById<ImageView>(R.id.iv_play_voice)
        private val audioProgressBar = itemView.findViewById<ProgressBar>(R.id.pb_audio)
        private val voiceProgressBar = itemView.findViewById<ProgressBar>(R.id.pb_voice)
        private val editedTextView = itemView.findViewById<MaterialTextView>(R.id.tv_edited)
        val sharedPref: SharedPreferences = context.getSharedPreferences("appData", Context.MODE_PRIVATE)
        val userId = sharedPref.getLong("ID",-1)
        @SuppressLint("SetTextI18n", "SuspiciousIndentation")
        fun bind(post: PostResponse) {
              if (post.authorAvatarUrl !="null"){
                  Glide.with(context)
                      .load(post.authorAvatarUrl)
                      .apply(RequestOptions.circleCropTransform())
                      .into(authorAvatarImageView)
              }
                   if (post.confirmed) confirmedImageView.show() else confirmedImageView.hide()
            authorAvatarImageView.setOnClickListener {
                it.anim()
                if (post.authorId!=userId) {
                    val switchingToUserPageActivity = Intent(context, UserPageActivity::class.java)
                    switchingToUserPageActivity.putExtra("USER_ID", post.authorId)
                    context.startActivity(switchingToUserPageActivity)
                }
            }
            authorUsername.text = post.authorUsername
            publicationDate.text = post.publishDate
            if (post.text!=null) {
                postTextView.show()
                postTextView.text = post.text
            } else   postTextView.hide()
            val imageUrl = post.imageUrl
            if (imageUrl !=null){
                mediaLinearLayout.show()
                postPictureImageView.show()
                Glide.with(context)
                    .load(imageUrl)
                    .into(postPictureImageView)

                postPictureImageView.setOnClickListener {
                    BigPictureDialog.newInstance(imageUrl).show((context as AppCompatActivity).supportFragmentManager,  "BigPictureDialog")
                }
            } else  postPictureImageView.hide()
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

            } else    videoRelativeLayout.hide()
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
            } else   audioLinearLayout.hide()



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
            } else  voiceLinearLayout.hide()
            likeCount.text = post.likesCount.toString()

              commentImageView.setOnClickListener {
                  it.anim()
                  val fragmentActivity = context as? FragmentActivity
                  val bsComment = CommentsBottomSheet.newInstance( postId = post.postId, userId = userId)
                  fragmentActivity?.supportFragmentManager?.let { it1 -> bsComment.show(it1," CommentsBottomSheet") }
              }
            if (post.isLikedIt){
                ViewCompat.setBackgroundTintList(likeImageView, ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.red)))

                likeImageView.setOnClickListener {
                    it.anim()
                    postViewModel.deleteLike(tokenManager.getAccessToken(),post.postId, onSuccess = {
                        post.isLikedIt = false
                        ViewCompat.setBackgroundTintList(likeImageView, ColorStateList.valueOf(
                            ContextCompat.getColor(context, R.color.gray)))
                           likeCount.text = post.likesCount--.toString()
                        notifyItemChanged(bindingAdapterPosition)
                    }, onError = {})
                }
            }else{
                ViewCompat.setBackgroundTintList(likeImageView, ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.gray)))
                likeImageView.setOnClickListener {
                    it.anim()
                    postViewModel.addLike(tokenManager.getAccessToken(),post.postId, onSuccess = {
                        post.isLikedIt = true
                        ViewCompat.setBackgroundTintList(likeImageView, ColorStateList.valueOf(
                            ContextCompat.getColor(context, R.color.red)))
                       likeCount.text = post.likesCount++.toString()
                        notifyItemChanged(bindingAdapterPosition)
                    }, onError = {})
                }

            }



            likeImageView.setOnLongClickListener {
                postFragment?.parentFragmentManager?.let { fr ->
                    RelationsDialogFragment(RelationType.LIKES, postId = post.postId).show(
                        fr, "RelationsDialogFragment"
                    )
                }
                    outsiderPostFragment?.parentFragmentManager?.let { fr ->
                        RelationsDialogFragment(RelationType.LIKES, postId = post.postId).show(
                            fr, "RelationsDialogFragment"
                        )
                    }

                subscriptionsPostsFeedFragment?.parentFragmentManager?.let { fr ->
                    RelationsDialogFragment(RelationType.LIKES, postId = post.postId).show(
                        fr, "RelationsDialogFragment"
                    )
                }

                    true
                }

            commentCount.text = post.commentsCount.toString()

            if(post.authorId ==userId){
                editPostButton.show()
                editPostButton.setOnClickListener {
                        it.showMenu(context, editAction = {
                            val dialog =  AddPostDialogFragment.newInstance(postId = post.postId, isEdit = true,text = post.text,post.imageUrl,post.videoUrl,post.audioUrl,post.voiceUrl)
                            if(postFragment!=null)
                                dialog.setAddPostDialogListener(postFragment)
                            dialog.show ((context as FragmentActivity).supportFragmentManager,"AddPostDialogFragment")
                        }, deleteAction = {
                            val dialog = ActionDialog(context)
                            dialog.setTitle(context.getString(R.string.delete_post_title))
                            dialog.setConfirmText(context.getString(R.string.delete_post_confirm_text))
                            dialog.setAction {
                                val token = tokenManager.getAccessToken()
                                postViewModel.deletePost(token, post.postId, onSuccess = { dialog.dismiss() }, onError = {})
                            }

                        })

                }


            } else editPostButton.hide()



            if (post.edited) editedTextView.show() else editedTextView.hide()
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