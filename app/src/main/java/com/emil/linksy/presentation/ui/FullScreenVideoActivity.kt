package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.emil.presentation.R

class FullScreenVideoActivity : AppCompatActivity() {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var playerView: PlayerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_video)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        playerView = findViewById(R.id.pv_fullscreen_video)
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        val videoUrl = intent.getStringExtra("VIDEO_URL") ?: return
        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))

        val backScreenButton: ImageButton = findViewById(R.id.ib_back)
        backScreenButton.setOnClickListener {
            finish()
        }

        playerView.setControllerVisibilityListener(PlayerView.ControllerVisibilityListener { visibility ->
            backScreenButton.visibility = if (visibility == View.VISIBLE) View.VISIBLE else View.GONE
        })

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.stop()
        exoPlayer.release()
    }
}
