package com.emil.linksy.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.ChannelPostResponse
import com.emil.linksy.presentation.ui.ActionDialog
import com.emil.linksy.presentation.ui.BigPictureDialog
import com.emil.linksy.presentation.ui.VideoPlayerDialogFragment
import com.emil.linksy.presentation.ui.navigation.channel.AddChannelPostDialogFragment
import com.emil.linksy.presentation.ui.navigation.channel.AppreciatedDialogFragment
import com.emil.linksy.presentation.ui.navigation.feed.ChannelPostsFeedFragment
import com.emil.linksy.presentation.ui.navigation.profile.CommentsBottomSheet
import com.emil.linksy.presentation.ui.page.ChannelPageActivity
import com.emil.linksy.presentation.viewmodel.ChannelViewModel
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.colorByRating
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.showMenu
import com.emil.presentation.R
import com.emil.presentation.databinding.RvItemChannelPostsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChannelPostsAdapter(private val tokenManager: TokenManager,
                          private val channelViewModel: ChannelViewModel,
                          val userId:Long,
                          private val channelId:Long = -100,
                          private val channelPageActivity: ChannelPageActivity?=null,
                          private val channelPostsFeedFragment:ChannelPostsFeedFragment? = null
): ListAdapter<ChannelPostResponse,ChannelPostsAdapter.ChatViewHolder>(ChannelPostDiffCallback ()) {


    companion object {
        private const val BIG_PICTURE_DIALOG_TAG = "BigPictureDialog"
        private const val VIDEO_PLAYER_DIALOG_FRAGMENT_TAG = "VideoPlayerDialogFragment"
        private const val ADD_POST_DIALOG_FRAGMENT_TAG = "AddPostDialogFragment"
        private const val COMMENTS_BOTTOM_SHEET_TAG = "CommentsBottomSheet"
        private const val APPRECIATED_DIALOG_FRAGMENT_TAG = "AppreciatedDialogFragment"
        private const val VIDEO_FRAME = 0L
        private const val PROGRESS_DELAY = 5L
        private const val START_PROGRESS = 0
    }

    inner class ChatViewHolder(private val binding:RvItemChannelPostsBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context
        private val channelAvatarImageView = binding.ivChannelAvatar
        private val postPictureImageView = binding.ivPostImage
        private val channelName = binding.tvChannelName
        private val publicationDate = binding.tvDate
        private val postTextView = binding.tvTextPostContent
        private val ratingTextView = binding.tvRatingAverage
        private val ratingImageView = binding.ivRating
        private val editPostButton = binding.ibEditPost
        private val videoRelativeLayout = binding.rlPostVideo
        private val mediaLinearLayout = binding.llMedia
        private val frameImageView = binding.ivFrame
        private val playVideoImageButton = binding.ibPlay
        private val audioProgressBar = binding.pbAudio
        private val audioLinearLayout = binding.llAudio
        private val pollTitleTextView = binding.tvTitle
        private val commentImageView = binding.ivComment
        private val commentTextView = binding.tvCommentCount
        private val pollLinearLayout = binding.llPoll
        private val playAudioButton = binding.ivPlayAudio
        private val optionRecyclerView = binding.rvOptions
        private val editedTextView = binding.tvEdited
        private val confirmedImageView = binding.ivConfirmed
        private val scoreLayout = binding.llScore
        private val scoreTextView = binding.tvScore
        private val deleteScoreButton = binding.ibDeleteScore
        val sharedPref: SharedPreferences = context.getSharedPreferences(Linksy.SHAREDPREF_MAIN_KEY, Context.MODE_PRIVATE)
        val userId = sharedPref.getLong(Linksy.SHAREDPREF_ID_KEY, Linksy.DEFAULT_ID)
        @SuppressLint("SetTextI18n")
        fun bind(post:ChannelPostResponse){
            val imageUrl = post.imageUrl
            val videoUrl = post.videoUrl
            val rating = post.averageRating
            var isPlayingAudio = false
            var mediaPlayerAudio: MediaPlayer? = null
            confirmedImageView.hide()
            postTextView.hide()
            postPictureImageView.hide()
            videoRelativeLayout.hide()
            audioLinearLayout.hide()
            pollLinearLayout.hide()
            editPostButton.hide()
            scoreLayout.hide()
            editedTextView.hide()
            channelAvatarImageView.setBackgroundResource(R.drawable.default_channel_avatar)
            channelName.text = post.channelName
            publicationDate.text = post.publishDate
            commentTextView.text = post.commentsCount.toString()
            ratingTextView.text = rating.toString()
            ratingImageView.colorByRating(rating)
            if (post.channelAvatarUrl != Linksy.EMPTY_AVATAR){
                Glide.with(context)
                    .load(post.channelAvatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(channelAvatarImageView)
            }

            if (post.confirmed) confirmedImageView.show()

            channelAvatarImageView.setOnClickListener {
                  it.anim()
                  val switchingToChannelPageActivity = Intent(context, ChannelPageActivity()::class.java)
                  switchingToChannelPageActivity.putExtra(Linksy.INTENT_CHANNEL_ID_KEY, post.channelId)
                  context.startActivity(switchingToChannelPageActivity)
               }

               if (post.text != null) {
                  postTextView.show()
                  postTextView.text = post.text
               }


            if (imageUrl != null){
                mediaLinearLayout.show()
                postPictureImageView.show()
                Glide.with(context)
                    .load(imageUrl)
                    .into(postPictureImageView)
                postPictureImageView.setOnClickListener {
                    BigPictureDialog.newInstance(imageUrl).show((context as AppCompatActivity).supportFragmentManager, BIG_PICTURE_DIALOG_TAG)
                }
            }


            if (videoUrl!=null){
                mediaLinearLayout.show()
                videoRelativeLayout.show()
                Glide.with(context)
                    .load(Uri.parse(videoUrl))
                    .frame(VIDEO_FRAME)
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
                        delay(PROGRESS_DELAY)
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
                        audioProgressBar.progress = START_PROGRESS
                        audioProgressBar.max = it.duration
                        isPlayingAudio = false
                    }

                    setOnCompletionListener {
                        audioProgressBar.progress = START_PROGRESS
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
                  if (post.options!=null){
                      pollLinearLayout.show()
                      pollTitleTextView.text = post.pollTitle
                      optionRecyclerView.layoutManager = LinearLayoutManager(context)
                      optionRecyclerView.adapter = OptionsAdapter(post.isVoted,post.options!!,channelViewModel,tokenManager)
                  }

    if (userId == post.authorId) {
    editPostButton.show()
    editPostButton.setOnClickListener {
        it.showMenu(context, editAction = {
            AddChannelPostDialogFragment
                .newInstance(channelId = channelId, postId = post.postId,
                    isEdit = true,text = post.text, imageUrl = post.imageUrl, videoUrl = post.videoUrl,
                    audioUrl = post.audioUrl, title = post.pollTitle,
                    options = post.options?.map { o -> o.optionText })
                .show ((context as FragmentActivity).supportFragmentManager, ADD_POST_DIALOG_FRAGMENT_TAG)

        }, deleteAction = {
            val dialog = ActionDialog(context)
            dialog.setTitle(context.getString(R.string.delete_post_title))
            dialog.setConfirmText(context.getString(R.string.delete_post_confirm_text))
            dialog.setAction {
                val token = tokenManager.getAccessToken()
                channelViewModel.deleteChannelPost(
                    token,
                    post.postId,
                    onSuccess = { dialog.dismiss()
                        channelPageActivity?.getData()
                        channelPostsFeedFragment?.getPosts() },
                    )
            }


        })

    }
}
            commentImageView.setOnClickListener {
                it.anim()
                val fragmentActivity = context as? FragmentActivity
                val bsComment = CommentsBottomSheet.newInstance( channelPostId = post.postId, userId = userId)
                fragmentActivity?.supportFragmentManager?.let { it1 -> bsComment.show(it1, COMMENTS_BOTTOM_SHEET_TAG) }
            }

            if (post.edited) editedTextView.show()

            if (post.userScore!=null) {
                scoreLayout.show()
                scoreTextView.text = post.userScore.toString()
                deleteScoreButton.setOnClickListener {
                    it.anim()
                    channelViewModel.deleteScore(tokenManager.getAccessToken(),post.postId, onSuccess = {
                        channelPageActivity?.getData()
                        channelPostsFeedFragment?.getPosts()
                        scoreLayout.hide()})

                }
            } else {
                ratingImageView.setOnClickListener {
                    it.anim()
                    showPopup(it, post.postId)
                }
            }

            ratingImageView.setOnLongClickListener {
                val dialog =  AppreciatedDialogFragment.newInstance(post.postId)
                dialog.show ((context as FragmentActivity).supportFragmentManager, APPRECIATED_DIALOG_FRAGMENT_TAG)
                true
            }
        }


    }

    @SuppressLint("InflateParams")
    private fun showPopup(anchor: View,postId:Long) {
        val inflater = LayoutInflater.from(anchor.context)
        val popupView = inflater.inflate(R.layout.popup_score, null)

        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popupHeight = popupView.measuredHeight


        val xOffset = 0
        val ySpace = 10
        val yOffset = -popupHeight - anchor.height - ySpace
        popupWindow.showAsDropDown(anchor, xOffset, yOffset)

        val scores = listOf(
            popupView.findViewById<TextView>(R.id.tv_1),
            popupView.findViewById(R.id.tv_2),
            popupView.findViewById(R.id.tv_3),
            popupView.findViewById(R.id.tv_4),
            popupView.findViewById(R.id.tv_5)
        )

        scores.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                addScore(postId, index + 1)
                popupWindow.dismiss()
            }
        }

    }

    private fun addScore (postId: Long,score:Int){
        channelViewModel.addScore(tokenManager.getAccessToken(),postId, score,
            onSuccess = {
            channelPageActivity?.getData()
            channelPostsFeedFragment?.getPosts()
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = RvItemChannelPostsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind (getItem(position))
    }

    @SuppressLint("DiffUtilEquals")
    class ChannelPostDiffCallback : DiffUtil.ItemCallback<ChannelPostResponse>() {
        override fun areItemsTheSame(oldItem: ChannelPostResponse, newItem: ChannelPostResponse): Boolean = oldItem.postId == newItem.postId
        override fun areContentsTheSame(oldItem: ChannelPostResponse, newItem: ChannelPostResponse): Boolean = oldItem == newItem
    }
}