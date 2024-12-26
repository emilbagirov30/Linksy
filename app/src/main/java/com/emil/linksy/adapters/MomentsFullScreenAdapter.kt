package com.emil.linksy.adapters

import android.content.Context
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
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emil.domain.model.MomentResponse
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.show
import com.emil.presentation.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MomentsFullScreenAdapter(
    private val momentsList: List<MomentResponse>,
    private val context: Context,
    private val parentFragment: DialogFragment
) : RecyclerView.Adapter<MomentsFullScreenAdapter.FullScreenViewHolder>() {

    private var activeViewHolder: FullScreenViewHolder? = null

    inner class FullScreenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val momentImageView: ImageView = itemView.findViewById(R.id.iv_moment_full)
        private val momentPlayerView: PlayerView = itemView.findViewById(R.id.pv_moment)
        private val momentTextView: TextView = itemView.findViewById(R.id.tv_moment)
        private val momentProgressBar: ProgressBar = itemView.findViewById(R.id.pb_moment)
        private var mediaPlayerAudio: MediaPlayer? = null
        private var progressJob: Job? = null
        private var totalDuration = 0
        private val updateInterval = 1L
        private var exoPlayer: ExoPlayer? = null
        fun bind(moment: MomentResponse) {
            releaseResources()
            momentProgressBar.progress = 0
            val videoUrl = moment.videoUrl
            val imageUrl = moment.imageUrl
            val audioUrl = moment.audioUrl
            val text = moment.text

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
                exoPlayer?.prepare()
                exoPlayer?.play()
            }

            if (text != null) {
                momentTextView.show()
                momentTextView.text = text
            }

            if (audioUrl != null) {
                mediaPlayerAudio = MediaPlayer().apply {
                    setDataSource(context, Uri.parse(audioUrl))
                    prepare()
                    start()
                }
            }

            startProgressBarUpdate()
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
                momentProgressBar.progress = 0
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
