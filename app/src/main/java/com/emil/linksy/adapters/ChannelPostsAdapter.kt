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
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.ChannelPostResponse
import com.emil.linksy.presentation.ui.ActionDialog
import com.emil.linksy.presentation.ui.BigPictureDialog
import com.emil.linksy.presentation.ui.VideoPlayerDialog
import com.emil.linksy.presentation.ui.navigation.channel.AddChannelPostDialogFragment
import com.emil.linksy.presentation.ui.navigation.channel.AppreciatedDialogFragment
import com.emil.linksy.presentation.ui.navigation.feed.ChannelPostsFeedFragment
import com.emil.linksy.presentation.ui.navigation.profile.AddPostDialogFragment
import com.emil.linksy.presentation.ui.navigation.profile.CommentsBottomSheet
import com.emil.linksy.presentation.ui.page.ChannelPageActivity
import com.emil.linksy.presentation.viewmodel.ChannelViewModel
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
import org.koin.java.KoinJavaComponent.inject

class ChannelPostsAdapter(private val postlist: List<ChannelPostResponse>,private val context: Context,private val tokenManager: TokenManager,private val channelViewModel: ChannelViewModel,
    val userId:Long,private val channelId:Long = -100,private val channelPageActivity: ChannelPageActivity?=null,private val channelPostsFeedFragment:ChannelPostsFeedFragment? = null
): RecyclerView.Adapter<ChannelPostsAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val channelAvatarImageView = itemView.findViewById<ImageView>(R.id.iv_channel_avatar)
        private val postPictureImageView = itemView.findViewById<ImageView>(R.id.iv_post_image)
        private val channelName = itemView.findViewById<MaterialTextView>(R.id.tv_channel_name)
        private val publicationDate = itemView.findViewById<MaterialTextView>(R.id.tv_date)
        private val postTextView = itemView.findViewById<MaterialTextView>(R.id.tv_text_post_content)
        private val ratingTextView = itemView.findViewById<MaterialTextView>(R.id.tv_rating_average)
        private val ratingImageView = itemView.findViewById<ImageView>(R.id.iv_rating)
        private val editPostButton = itemView.findViewById<ImageButton>(R.id.ib_edit_post)
        private val videoRelativeLayout = itemView.findViewById<RelativeLayout>(R.id.rl_post_video)
        private val mediaLinearLayout = itemView.findViewById<LinearLayout>(R.id.ll_media)
        private val frameImageView = itemView.findViewById<ImageView>(R.id.iv_frame)
        private val playVideoImageButton = itemView.findViewById<ImageButton>(R.id.ib_play)
        private val audioProgressBar = itemView.findViewById<ProgressBar>(R.id.pb_audio)
        private val audioLinearLayout = itemView.findViewById<LinearLayout>(R.id.ll_audio)
        private val pollTitleTextView = itemView.findViewById<MaterialTextView>(R.id.tv_title)
        private val commentImageView = itemView.findViewById<ImageView>(R.id.iv_comment)
        private val commentTextView = itemView.findViewById<MaterialTextView>(R.id.tv_comment_count)
        private val pollLinearLayout = itemView.findViewById<LinearLayout>(R.id.ll_poll)
        private val playAudioButton = itemView.findViewById<ImageView>(R.id.iv_play_audio)
        private val optionRecyclerView = itemView.findViewById<RecyclerView>(R.id.rv_options)
        private val editedTextView = itemView.findViewById<MaterialTextView>(R.id.tv_edited)
        private val scoreLayout = itemView.findViewById<LinearLayout>(R.id.ll_score)
        private val scoreTextView = itemView.findViewById<MaterialTextView>(R.id.tv_score)
        private val deleteScoreButton = itemView.findViewById<ImageButton>(R.id.ib_delete_score)
        val sharedPref: SharedPreferences = context.getSharedPreferences("appData", Context.MODE_PRIVATE)
        val userId = sharedPref.getLong("ID",-1)
        @SuppressLint("SuspiciousIndentation", "SetTextI18n")
        fun bind(post:ChannelPostResponse){
            if (post.channelAvatarUrl !="null"){
                Glide.with(context)
                    .load(post.channelAvatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(channelAvatarImageView)
            }


          channelAvatarImageView.setOnClickListener {
              it.anim()
              val switchingToChannelPageActivity =
                  Intent(context, ChannelPageActivity()::class.java)
              switchingToChannelPageActivity.putExtra("CHANNEL_ID", post.channelId)
              context.startActivity(switchingToChannelPageActivity)
          }
           channelName.text = post.channelName
            publicationDate.text = post.publishDate
            if (post.text!=null) {
                postTextView.show()
                postTextView.text = post.text
            } else postTextView.hide()
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
            }else    postPictureImageView.hide()
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

            } else  videoRelativeLayout.hide()
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
            } else   audioLinearLayout.hide()

                   var rating = post.averageRating
                ratingTextView.text = rating.toString()
            if (rating<2.99 && rating>0)
                ViewCompat.setBackgroundTintList(ratingImageView, ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.red)))
            if(rating >= 3.0 && rating < 4.0)  ViewCompat.setBackgroundTintList(ratingImageView, ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.yellow)))
            if(rating >=4)  ViewCompat.setBackgroundTintList(ratingImageView, ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.green)))
                  if (post.options!=null){
                      pollLinearLayout.show()
                      pollTitleTextView.text = post.pollTitle
                      optionRecyclerView.layoutManager = LinearLayoutManager(context)
                      optionRecyclerView.adapter = OptionsAdapter(post.isVoted,post.options!!,channelViewModel,tokenManager)
                  } else  pollLinearLayout.hide()

if (userId == post.authorId) {
    editPostButton.show()
    editPostButton.setOnClickListener {
        it.showMenu(context, editAction = {
            AddChannelPostDialogFragment
                .newInstance(channelId = channelId, postId = post.postId, isEdit = true,text = post.text,post.imageUrl,post.videoUrl,post.audioUrl,post.pollTitle,
                    post.options?.map { o -> o.optionText })
                .show ((context as FragmentActivity).supportFragmentManager,"AddPostDialogFragment")

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
                        channelPostsFeedFragment?.getPosts()

                                },
                    onConflict = {})
            }


        })

    }
} else editPostButton.hide()
            commentTextView.text = post.commentsCount.toString()
            commentImageView.setOnClickListener {
                it.anim()
                val fragmentActivity = context as? FragmentActivity
                val bsComment = CommentsBottomSheet.newInstance( channelPostId = post.postId, userId = userId)
                fragmentActivity?.supportFragmentManager?.let { it1 -> bsComment.show(it1," CommentsBottomSheet") }
            }

            if (post.edited) editedTextView.show() else editedTextView.hide()

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
            }else {
                   scoreLayout.hide()
                ratingImageView.setOnClickListener {
                    it.anim()
                    showPopup(it, post.postId)
                }
            }

            ratingImageView.setOnLongClickListener {
                val dialog =  AppreciatedDialogFragment.newInstance(post.postId)
                dialog.show ((context as FragmentActivity).supportFragmentManager,"AppreciatedDialogFragment")
                true
            }
        }


    }
    @SuppressLint("InflateParams")
    private fun showPopup(anchor: View,postId:Long) {
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_score, null)

        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popupHeight = popupView.measuredHeight


        val xOffset = 0
        val yOffset = -popupHeight - anchor.height - 10
        popupWindow.showAsDropDown(anchor, xOffset, yOffset)

        val score1 = popupView.findViewById<TextView>(R.id.tv_1)
        val score2 = popupView.findViewById<TextView>(R.id.tv_2)
        val score3 = popupView.findViewById<TextView>(R.id.tv_3)
        val score4 = popupView.findViewById<TextView>(R.id.tv_4)
        val score5 = popupView.findViewById<TextView>(R.id.tv_5)

        score1.setOnClickListener {
            addScore(postId,1)
        }
        score2.setOnClickListener {
            addScore(postId,2)
        }
        score3.setOnClickListener {
            addScore(postId,3)
        }
        score4.setOnClickListener {
            addScore(postId,4)
        }
        score5.setOnClickListener {
            addScore(postId,5)
        }


    }



    private fun addScore (postId: Long,score:Int){
        channelViewModel.addScore(tokenManager.getAccessToken(),postId, score, onSuccess = {  channelPageActivity?.getData()
        channelPostsFeedFragment?.getPosts()

        })
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