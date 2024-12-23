package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.emil.linksy.presentation.viewmodel.PostViewModel
import com.emil.linksy.util.ContentType
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.createAudioFilePart
import com.emil.linksy.util.createContentPicker
import com.emil.linksy.util.createImageFilePart
import com.emil.linksy.util.createVideoFilePart
import com.emil.linksy.util.createVoiceFilePart
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.string
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class AddPostDialogFragment (private val postFragment:PostFragment): DialogFragment() {

    private lateinit var toolBar: MaterialToolbar
    private val postViewModel: PostViewModel by viewModel<PostViewModel>()
    private lateinit var postEditText: EditText
    private lateinit var publishButton:MaterialButton
    private lateinit var addPictureButton: MaterialButton
    private lateinit var addVideoButton: MaterialButton
    private lateinit var addAudioButton: MaterialButton
    private lateinit var addVoiceButton: MaterialButton
    private lateinit var pickedPictureImageView:ImageView
    private lateinit var pickedVideoVideoView:VideoView
    private lateinit var deletePictureButton:ImageButton
    private lateinit var deleteVideoButton:ImageButton
    private lateinit var playAudio:ImageView
    private lateinit var playVoice:ImageView
    private lateinit var audioProgressBar:ProgressBar
    private lateinit var voiceProgressBar: ProgressBar
    private lateinit var deleteVoice:ImageButton
    private lateinit var deleteAudio:ImageButton
    private lateinit var mediaLinearLayout: LinearLayout
    private lateinit var  pictureFrameLayout: FrameLayout
    private lateinit var videoFrameLayout: FrameLayout
    private lateinit var audioLinearLayout: LinearLayout
    private lateinit var voiceLinearLayout: LinearLayout
    private lateinit var mediaPlayerAudio: MediaPlayer
    private lateinit var mediaPlayerVoice: MediaPlayer
    private var isPlayingAudio = false
    private var isPlayingVoice = false
    private var imageUri:Uri? = null
    private var videoUri:Uri? = null
    private var audioUri:Uri? = null
    private var voiceUri:Uri? = null
    private val tokenManager: TokenManager by inject()
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_post_dialog, container, false)
        postEditText = view.findViewById(R.id.et_post)
        publishButton = view.findViewById(R.id.bt_publish)
        addPictureButton = view.findViewById(R.id.bt_add_picture)
        addVideoButton = view.findViewById(R.id.bt_add_video)
        addAudioButton = view.findViewById(R.id.bt_add_audio)
        addVoiceButton = view.findViewById(R.id.bt_add_voice)
        pickedPictureImageView = view.findViewById(R.id.iv_picked_picture)
        pickedVideoVideoView = view.findViewById(R.id.vv_picked_video)
        deletePictureButton = view.findViewById(R.id.ib_delete_picture)
        deleteVideoButton = view.findViewById(R.id.ib_delete_video)
        playAudio =  view.findViewById(R.id.iv_play_audio)
        playVoice =  view.findViewById(R.id.iv_play_voice)
        audioProgressBar =  view.findViewById(R.id.pb_picked_audio)
        voiceProgressBar =  view.findViewById(R.id.pb_voice)
        deleteVoice = view.findViewById(R.id.ib_delete_voice)
        deleteAudio = view.findViewById(R.id.ib_delete_audio)
        toolBar = view.findViewById(R.id.tb_edit_data)
        mediaLinearLayout = view.findViewById(R.id.ll_picked_media)
        audioLinearLayout = view.findViewById(R.id.ll_picked_audio)
        voiceLinearLayout = view.findViewById(R.id.ll_voice)
        pictureFrameLayout =  view.findViewById(R.id.fl_picture)
        videoFrameLayout =  view.findViewById(R.id.fl_video)
        toolBar.setNavigationOnClickListener { dialog?.dismiss() }

        addVoiceButton.setOnClickListener {
            it.anim()
            val dialog = VoiceRecordDialog(this)
        }
        val pickImageLauncher = createContentPicker(this) { uri ->
            handleSelectedImage(uri)
            imageUri = uri
            updatePublishButtonState()
        }
        val pickVideoLauncher = createContentPicker(this) { uri ->
            handleSelectedVideo(uri)
            videoUri = uri
            updatePublishButtonState()
        }
        val pickAudioLauncher = createContentPicker(this) { uri ->
            handleSelectedAudio(uri)
            audioUri = uri
            updatePublishButtonState()
        }
        addPictureButton.setOnClickListener {
            it.anim()
            pickImageLauncher.launch(ContentType.IMAGE.mimeType)
        }

        addVideoButton.setOnClickListener {
            it.anim()
        pickVideoLauncher.launch(ContentType.VIDEO.mimeType)
        }

        addAudioButton.setOnClickListener {
            it.anim()
         pickAudioLauncher.launch(ContentType.AUDIO.mimeType)
}

        postEditText.addTextChangedListener { updatePublishButtonState() }


        publishButton.setOnClickListener {
            it.anim()
           val loading = LoadingDialog(requireContext())
            loading.show()
            val text = postEditText.string()
            val token = tokenManager.getAccessToken()
            val imagePart = imageUri?.let { createImageFilePart(requireContext(), it) }
            val videoPart = videoUri?.let { createVideoFilePart(requireContext(), it) }
            val audioPart = audioUri?.let { createAudioFilePart(requireContext(), it) }
            val voicePart = voiceUri?.let { createVoiceFilePart(requireContext(), it) }

            postViewModel.publishPost(token,text,
                image = imagePart, video = videoPart,
                audio = audioPart,voice = voicePart,
                onSuccess = {
                    postFragment.updatePosts()
                    dialog?.dismiss()
                    loading.dismiss()
                })
        }


        return  view
    }
    private fun handleSelectedImage(uri: Uri) {
        mediaLinearLayout.show()
        pictureFrameLayout.show()
        Glide.with(requireContext())
            .load(uri)
            .into(pickedPictureImageView)

        deletePictureButton.setOnClickListener {
            it.anim()
           pictureFrameLayout.hide()
            if (!videoFrameLayout.isVisible) mediaLinearLayout.hide()
            imageUri = null
            updatePublishButtonState()
        }
    }

    private fun handleSelectedVideo(uri: Uri) {
        mediaLinearLayout.show()
        videoFrameLayout.show()
        pickedVideoVideoView.setVideoURI(uri)
        pickedVideoVideoView.setOnPreparedListener { mp ->
            mp.setVolume(0f, 0f)
            pickedVideoVideoView.start()
        }
        pickedVideoVideoView.setOnCompletionListener {
            pickedVideoVideoView.start()
        }
        deleteVideoButton.setOnClickListener {
            it.anim()
            pickedVideoVideoView.stopPlayback()
            videoFrameLayout.hide()
            if (!pictureFrameLayout.isVisible) mediaLinearLayout.hide()
            videoUri = null
            updatePublishButtonState()
        }
    }
    private fun handleSelectedAudio(uri: Uri) {

        audioLinearLayout.show()
        playAudio.setImageResource(R.drawable.ic_play)
        mediaPlayerAudio = MediaPlayer().apply {
            setDataSource(requireContext(), uri)
            prepareAsync()
            setOnPreparedListener {
                isPlayingAudio = false
                audioProgressBar.progress = 0
                audioProgressBar.max = it.duration

            }
            setOnCompletionListener {
                isPlayingAudio = false
                audioProgressBar.progress = 0
                playAudio.setImageResource(R.drawable.ic_play)
            }
        }


        playAudio.setOnClickListener {
            if (isPlayingAudio) {
                mediaPlayerAudio.pause()
                playAudio.setImageResource(R.drawable.ic_play)
                isPlayingAudio = false
            } else {
                mediaPlayerAudio.start()
                playAudio.setImageResource(R.drawable.ic_pause)
                isPlayingAudio = true
                startProgressAudioUpdate()
            }
        }


        deleteAudio.setOnClickListener {
            it.anim()
            mediaPlayerAudio.stop()
            mediaPlayerAudio.reset()
            isPlayingAudio = false
            audioProgressBar.progress = 0
            audioLinearLayout.hide()
            audioUri = null
            updatePublishButtonState()
        }


    }

    private fun startProgressAudioUpdate() {

        CoroutineScope(Dispatchers.Main).launch {
            while (mediaPlayerAudio.isPlaying) {
                val currentPosition = mediaPlayerAudio.currentPosition
                val totalDuration = mediaPlayerAudio.duration
                audioProgressBar.max = totalDuration
                audioProgressBar.progress = currentPosition
                delay(5)
            }
        }
    }

    private fun startProgressVoiceUpdate() {
        CoroutineScope(Dispatchers.Main).launch {
            while (mediaPlayerVoice.isPlaying) {
                val currentPosition = mediaPlayerVoice.currentPosition
                val totalDuration = mediaPlayerVoice.duration
                voiceProgressBar.max = totalDuration
                voiceProgressBar.progress = currentPosition
                delay(5)
            }
        }
    }
    override fun getTheme() = R.style.FullScreenDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }

    fun handleVoiceRecordingSaved(recordedFile: File?) {
        if (recordedFile != null) {
            voiceLinearLayout.show()
            setupVoicePlayback(recordedFile)
            updatePublishButtonState()
        }
    }
    private fun setupVoicePlayback(file: File) {
        val uri = Uri.fromFile(file)
        voiceUri = uri
        mediaPlayerVoice = MediaPlayer().apply {
            setDataSource(requireContext(), uri)
            prepareAsync()
            setOnPreparedListener {
                playVoice.setImageResource(R.drawable.ic_play)
            }
            setOnCompletionListener {
                playVoice.setImageResource(R.drawable.ic_play)
                voiceProgressBar.progress = 0
                isPlayingVoice = false
            }
            deleteVoice.setOnClickListener {
                it.anim()
                mediaPlayerVoice.stop()
                mediaPlayerVoice.reset()
                isPlayingVoice = false
                voiceLinearLayout.hide()
                voiceUri = null
                updatePublishButtonState()
            }
        }

        playVoice.setOnClickListener {
            if (isPlayingVoice) {
                mediaPlayerVoice.pause()
                playVoice.setImageResource(R.drawable.ic_play)
                isPlayingVoice = false
            } else {
                mediaPlayerVoice.start()
                playVoice.setImageResource(R.drawable.ic_pause)
                isPlayingVoice = true

                startProgressVoiceUpdate()
            }

        }
    }
    private fun updatePublishButtonState() {
        val isMediaSelected = imageUri != null || videoUri != null || audioUri != null || voiceUri != null
        val isTextNotEmpty = postEditText.string().isNotEmpty()
        publishButton.isEnabled = isMediaSelected || isTextNotEmpty
    }
}