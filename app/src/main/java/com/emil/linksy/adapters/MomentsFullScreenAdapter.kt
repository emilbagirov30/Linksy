package com.emil.linksy.adapters

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.MomentResponse
import com.emil.linksy.presentation.ui.page.UserPageActivity
import com.emil.linksy.presentation.viewmodel.MomentViewModel
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MomentsFullScreenAdapter(
    private val momentsList: List<MomentResponse>,
    private val context: Context,
    private val parentFragment: DialogFragment,
    private val momentViewModel: MomentViewModel,
    private val tokenManager: TokenManager
) : RecyclerView.Adapter<MomentsFullScreenAdapter.FullScreenViewHolder>() {

    private var activeViewHolder: FullScreenViewHolder? = null


    companion object {
        const val START_PROGRESS = 0
    }

    inner class FullScreenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val momentImageView: ImageView = itemView.findViewById(R.id.iv_moment_full)
        private val momentPlayerView: PlayerView = itemView.findViewById(R.id.pv_moment)
        private val momentTextView: TextView = itemView.findViewById(R.id.tv_moment)
        private val dateTextView: TextView = itemView.findViewById(R.id.tv_date)
        private val momentProgressBar: ProgressBar = itemView.findViewById(R.id.pb_moment)
        private val authorAvatarImageView:ImageView = itemView.findViewById(R.id.iv_avatar)
        private val confirmedImageView:ImageView = itemView.findViewById(R.id.iv_confirmed)
        private val authorNameTextView:MaterialTextView = itemView.findViewById(R.id.tv_name)
        private var mediaPlayerAudio: MediaPlayer? = null
        private var progressJob: Job? = null
        private var totalDuration = 0
        private val updateInterval = 1L
        private var exoPlayer: ExoPlayer? = null
        val sharedPref: SharedPreferences = context.getSharedPreferences(Linksy.SHAREDPREF_MAIN_KEY, Context.MODE_PRIVATE)
        val userId = sharedPref.getLong(Linksy.SHAREDPREF_ID_KEY, Linksy.DEFAULT_ID)

        fun bind(moment: MomentResponse) {
            releaseResources()
            momentProgressBar.progress = START_PROGRESS
            val videoUrl = moment.videoUrl
            val imageUrl = moment.imageUrl
            val audioUrl = moment.audioUrl
            val text = moment.text

            if(moment.confirmed) confirmedImageView.show() else confirmedImageView.hide()
            authorNameTextView.text = moment.authorUsername

            if (moment.authorAvatarUrl!= Linksy.EMPTY_AVATAR) {
                Glide.with(context)
                    .load(moment.authorAvatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(authorAvatarImageView)
            } else authorAvatarImageView.setBackgroundResource(R.drawable.default_avatar)

            momentViewModel.viewMoment(tokenManager.getAccessToken(),moment.momentId)

            authorAvatarImageView.setOnClickListener {
               if(userId!=moment.authorId){
                   val switchingToUserPageActivity = Intent(context, UserPageActivity::class.java)
                   switchingToUserPageActivity.putExtra(Linksy.INTENT_USER_ID_KEY, moment.authorId)
                   context.startActivity(switchingToUserPageActivity)
               }
           }

            if (imageUrl != null) {
                totalDuration = Linksy.MOMENT_SHORT_DURATION * 1000
                momentImageView.show()
                Glide.with(context)
                    .load(imageUrl)
                    .into(momentImageView)
            }

            if (videoUrl != null) {
                totalDuration = Linksy.MOMENT_LONG_DURATION * 1000
                momentPlayerView.show()
                exoPlayer = ExoPlayer.Builder(context).build()
                momentPlayerView.player = exoPlayer
                val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
                exoPlayer?.setMediaItem(mediaItem)
                exoPlayer?.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_READY) {
                            val videoDuration = (exoPlayer?.duration?: 0L).toInt()
                           if (videoDuration<totalDuration)
                               totalDuration = videoDuration
                              startProgressBarUpdate()
                        }
                    }
                })
                exoPlayer?.prepare()
                exoPlayer?.play()
            }

            if (text != null) {
                momentTextView.show()
                momentTextView.text = text
            }
            dateTextView.text = moment.publishDate

            if (audioUrl != null) {
                mediaPlayerAudio = MediaPlayer().apply {
                    setDataSource(context, Uri.parse(audioUrl))
                    prepare()
                    start()
                }
            }


        }

        fun releaseResources() {
            if (exoPlayer?.isPlaying == true) {
               exoPlayer?.stop()
                exoPlayer?.release()
            }
            mediaPlayerAudio?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            }
            mediaPlayerAudio = null
        }

        fun startProgressBarUpdate() {
            progressJob?.cancel()
            progressJob = CoroutineScope(Dispatchers.Main).launch {
                var progress = 0
                momentProgressBar.progress = START_PROGRESS
                momentProgressBar.max = (totalDuration / updateInterval).toInt()
                while (progress <= momentProgressBar.max) {
                    momentProgressBar.progress = progress
                    delay(updateInterval)
                    progress++
                }

                releaseResources()
                val nextPosition = bindingAdapterPosition + 1
                if (nextPosition < momentsList.size)
                    (itemView.parent as? RecyclerView)?.smoothScrollToPosition(nextPosition) else
                    parentFragment.dismiss()
            }
        }

        fun cancelProgressUpdate() {
            progressJob?.cancel()
            progressJob = null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullScreenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_fullscreen_moment, parent, false)
        return FullScreenViewHolder(view)
    }

    override fun getItemCount(): Int = momentsList.size

    override fun onBindViewHolder(holder: FullScreenViewHolder, position: Int) {
        holder.bind(momentsList[position])
        activeViewHolder?.releaseResources()
        activeViewHolder = holder
    }

    override fun onViewAttachedToWindow(holder: FullScreenViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.startProgressBarUpdate()
    }

    override fun onViewDetachedFromWindow(holder: FullScreenViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.cancelProgressUpdate()
        holder.releaseResources()
    }

    fun releaseAllResources() {
        activeViewHolder?.releaseResources()
        activeViewHolder = null
    }
}
