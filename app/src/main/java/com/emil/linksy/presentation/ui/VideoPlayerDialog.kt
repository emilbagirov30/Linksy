package com.emil.linksy.presentation.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.emil.presentation.R


class VideoPlayerDialog(private val context: Context, private val videoUrl: String) : Dialog(context, R.style.RoundedDialog) {
    private var playerView: PlayerView
    private val exoPlayer: ExoPlayer

    init {
        setContentView(R.layout.video_player_dialog)
        playerView = findViewById(R.id.pv_video)
        exoPlayer = ExoPlayer.Builder(context).build()
        playerView.player = exoPlayer
        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        show()


        val fullscreenButton: ImageButton = findViewById(R.id.ib_fullscreen)
        fullscreenButton.setOnClickListener {
            toggleFullScreen()
        }
        playerView.setControllerVisibilityListener(PlayerView.ControllerVisibilityListener {
                visibility -> fullscreenButton.visibility = if (visibility == View.VISIBLE) View.VISIBLE else View.GONE
        })
    }

    private fun toggleFullScreen() {
        val intent = Intent(context, FullScreenVideoActivity::class.java)
        intent.putExtra("VIDEO_URL", videoUrl)
        context.startActivity(intent)
    }

    override fun dismiss() {
        super.dismiss()
        exoPlayer.stop()
        exoPlayer.release()
    }
}
