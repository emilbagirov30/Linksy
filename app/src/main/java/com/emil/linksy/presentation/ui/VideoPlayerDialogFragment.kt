package com.emil.linksy.presentation.ui


import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.fragment.app.DialogFragment
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.emil.presentation.R
import com.emil.presentation.databinding.VideoPlayerDialogBinding


class VideoPlayerDialogFragment: DialogFragment () {

    private val exoPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(requireActivity()).build()
    }
    private var videoUrl: String? = null
    private lateinit var binding:VideoPlayerDialogBinding
    companion object {
        private const val ARG_VIDEO_URL = "VIDEO_URL"
        private const val ORIENTATION_RESET_DELAY = 1000L
        fun newInstance(url: String?): VideoPlayerDialogFragment {
            val fragment = VideoPlayerDialogFragment()
            val args = Bundle()
            args.putString(ARG_VIDEO_URL, url)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(android.R.style.Animation_Dialog)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                decorView.windowInsetsController?.hide(WindowInsets.Type.navigationBars())
            else {
                @Suppress("DEPRECATION")
                decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        )
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoUrl = arguments?.getString(ARG_VIDEO_URL)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = VideoPlayerDialogBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.playerView.player = exoPlayer
        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
        binding.playerView.setFullscreenButtonClickListener {
           toggleFullscreen()
        }
        binding.playerView.setControllerVisibilityListener(PlayerView.ControllerVisibilityListener { visibility ->
            binding.closeButton.visibility = if (visibility == View.VISIBLE) View.VISIBLE else View.GONE
        })
        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun getTheme() = R.style.FullScreenDialog

    @SuppressLint("SourceLockedOrientationActivity")
    private fun toggleFullscreen() {
        val activity = requireActivity()
        val currentOrientation = resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

    }

    override fun onStop() {
        super.onStop()
        if (!requireActivity().isChangingConfigurations) {
            exoPlayer.playWhenReady = false
        }
    }

    override fun dismiss() {
        super.dismiss()
        exoPlayer.stop()
        exoPlayer.release()
    }
}
