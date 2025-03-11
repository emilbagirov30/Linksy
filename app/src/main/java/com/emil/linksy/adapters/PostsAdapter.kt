package com.emil.linksy.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.PostResponse
import com.emil.linksy.presentation.ui.ActionDialog
import com.emil.linksy.presentation.ui.BigPictureDialog
import com.emil.linksy.presentation.ui.VideoPlayerDialogFragment
import com.emil.linksy.presentation.ui.navigation.feed.SubscriptionsPostsFeedFragment
import com.emil.linksy.presentation.ui.navigation.people.RelationsDialogFragment
import com.emil.linksy.presentation.ui.navigation.profile.AddPostDialogFragment
import com.emil.linksy.presentation.ui.navigation.profile.CommentsBottomSheet
import com.emil.linksy.presentation.ui.navigation.profile.PostFragment
import com.emil.linksy.presentation.ui.page.OutsiderPostFragment
import com.emil.linksy.presentation.ui.page.UserPageActivity
import com.emil.linksy.presentation.viewmodel.PostViewModel
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.RelationType
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.showMenu
import com.emil.presentation.R
import com.emil.presentation.databinding.RvItemPostsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PostsAdapter(private val postList: List<PostResponse>, private val postViewModel: PostViewModel,
                   private val tokenManager: TokenManager, private val postFragment: PostFragment?=null,
                   private val outsiderPostFragment: OutsiderPostFragment?=null,private val subscriptionsPostsFeedFragment: SubscriptionsPostsFeedFragment? = null
    ): RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

        companion object {
            private const val BIG_PICTURE_DIALOG_TAG = "BigPictureDialog"
            private const val VIDEO_PLAYER_DIALOG_FRAGMENT_TAG = "VideoPlayerDialogFragment"
            private const val ADD_POST_DIALOG_FRAGMENT_TAG = "AddPostDialogFragment"
            private const val COMMENTS_BOTTOM_SHEET_TAG = "CommentsBottomSheet"
            private const val RELATIONS_DIALOG_FRAGMENT_TAG = "RelationsDialogFragment"
        }
    inner class PostsViewHolder(binding: RvItemPostsBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context
        private val authorAvatarImageView = binding.ivAuthorAvatar
        private val postPictureImageView = binding.ivPostImage
        private val authorUsername = binding.tvAuthorUsername
        private val publicationDate = binding.tvDate
        private val postTextView = binding.tvTextPostContent
        private val likeCount = binding.tvLikeCount
        private val likeImageView = binding.ivLike
        private val commentImageView = binding.ivComment
        private val commentCount = binding.tvCommentCount
        private val editPostButton = binding.ibEditPost
        private val videoRelativeLayout = binding.rlPostVideo
        private val mediaLinearLayout = binding.llMedia
        private val frameImageView = binding.ivFrame
        private val playVideoImageButton = binding.ibPlay
        private val audioLinearLayout = binding.llAudio
        private val voiceLinearLayout = binding.llVoice
        private val confirmedImageView = binding.ivConfirmed
        private val playAudioButton = binding.ivPlayAudio
        private val playVoiceButton = binding.ivPlayVoice
        private val audioProgressBar = binding.pbAudio
        private val voiceProgressBar = binding.pbVoice
        private val editedTextView = binding.tvEdited
        val sharedPref: SharedPreferences = context.getSharedPreferences(Linksy.SHAREDPREF_MAIN_KEY, Context.MODE_PRIVATE)
        val userId = sharedPref.getLong(Linksy.SHAREDPREF_ID_KEY,Linksy.DEFAULT_ID)
        @SuppressLint("SetTextI18n", "SuspiciousIndentation")
        fun bind(post: PostResponse) {
            postTextView.hide()
            postPictureImageView.hide()
            videoRelativeLayout.hide()
            audioLinearLayout.hide()
            voiceLinearLayout.hide()
            editPostButton.hide()
            editedTextView.hide()
            confirmedImageView.hide()
            authorAvatarImageView.setBackgroundResource(R.drawable.default_avatar)
            authorUsername.text = post.authorUsername
            publicationDate.text = post.publishDate
            commentCount.text = post.commentsCount.toString()
            likeCount.text = post.likesCount.toString()
            val imageUrl = post.imageUrl
            val videoUrl = post.videoUrl
            var isPlayingAudio = false
            var mediaPlayerAudio:MediaPlayer? = null
            var isPlayingVoice = false
            var mediaPlayerVoice:MediaPlayer? = null

              if (post.authorAvatarUrl != Linksy.EMPTY_AVATAR) {
                  Glide.with(context)
                      .load(post.authorAvatarUrl)
                      .apply(RequestOptions.circleCropTransform())
                      .into(authorAvatarImageView)
              }
                   if (post.confirmed) confirmedImageView.show()

                    authorAvatarImageView.setOnClickListener {
                     it.anim()
                     if (post.authorId!=userId) {
                     val switchingToUserPageActivity = Intent(context, UserPageActivity::class.java)
                     switchingToUserPageActivity.putExtra(Linksy.INTENT_USER_ID_KEY, post.authorId)
                     context.startActivity(switchingToUserPageActivity)
                     } }



            if (post.text != null) {
                postTextView.show()
                postTextView.text = post.text
            }



            if (imageUrl !=null){
                mediaLinearLayout.show()
                postPictureImageView.show()
                Glide.with(context)
                    .load(imageUrl)
                    .into(postPictureImageView)
                postPictureImageView.setOnClickListener {
                    BigPictureDialog.newInstance(imageUrl).show((context as AppCompatActivity).supportFragmentManager,  BIG_PICTURE_DIALOG_TAG)
                }
            }



            if (videoUrl!=null){
                mediaLinearLayout.show()
                videoRelativeLayout.show()
                Glide.with(context)
                    .load(Uri.parse(videoUrl))
                    .frame(0)
                    .into(frameImageView)
                playVideoImageButton.setOnClickListener{
                    val playerDialog = VideoPlayerDialogFragment.newInstance(url = videoUrl)
                    playerDialog.show((context as AppCompatActivity).supportFragmentManager, VIDEO_PLAYER_DIALOG_FRAGMENT_TAG)
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



              commentImageView.setOnClickListener {
                  it.anim()
                  val fragmentActivity = context as? FragmentActivity
                  val bsComment = CommentsBottomSheet.newInstance( postId = post.postId, userId = userId)
                  fragmentActivity?.supportFragmentManager?.let { it1 -> bsComment.show(it1,
                      COMMENTS_BOTTOM_SHEET_TAG) }
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
            } else {
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
                    })
                }

            }



            likeImageView.setOnLongClickListener {
                postFragment?.parentFragmentManager?.let { fr ->
                    RelationsDialogFragment.newInstance(RelationType.LIKES, postId = post.postId).show(
                        fr, RELATIONS_DIALOG_FRAGMENT_TAG
                    )
                }
                    outsiderPostFragment?.parentFragmentManager?.let { fr ->
                        RelationsDialogFragment.newInstance(RelationType.LIKES, postId = post.postId).show(
                            fr, RELATIONS_DIALOG_FRAGMENT_TAG
                        )
                    }

                subscriptionsPostsFeedFragment?.parentFragmentManager?.let { fr ->
                    RelationsDialogFragment.newInstance(RelationType.LIKES, postId = post.postId).show(
                        fr, RELATIONS_DIALOG_FRAGMENT_TAG
                    )
                }

                    true
                }



            if(post.authorId ==userId) {
                editPostButton.show()
                editPostButton.setOnClickListener {
                        it.showMenu(context, editAction = {
                            val dialog =  AddPostDialogFragment.newInstance(
                                postId = post.postId, isEdit = true,
                                text = post.text, imageUrl = post.imageUrl,
                                videoUrl = post.videoUrl, audioUrl = post.audioUrl,
                                voiceUrl = post.voiceUrl)
                            if(postFragment!=null)
                                dialog.setAddPostDialogListener(postFragment)
                            dialog.show ((context as FragmentActivity).supportFragmentManager,
                                ADD_POST_DIALOG_FRAGMENT_TAG)
                        }, deleteAction = {
                            val dialog = ActionDialog(context)
                            dialog.setTitle(context.getString(R.string.delete_post_title))
                            dialog.setConfirmText(context.getString(R.string.delete_post_confirm_text))
                            dialog.setAction {
                                val token = tokenManager.getAccessToken()
                                postViewModel.deletePost(token, post.postId, onSuccess = {
                                    dialog.dismiss()
                                })
                            }

                        })
                }
            }

            if (post.edited) editedTextView.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val binding = RvItemPostsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostsViewHolder(binding)
    }

    override fun getItemCount(): Int = postList.size


    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        holder.bind (postList[position])
    }

}