package com.emil.linksy.presentation.ui.navigation.profile

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.emil.linksy.presentation.ui.LoadingDialog
import com.emil.linksy.presentation.ui.navigation.profile.AddPostDialogFragment.AddPostDialogListener
import com.emil.linksy.presentation.viewmodel.MomentViewModel
import com.emil.linksy.util.ContentType
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.createAudioFilePart
import com.emil.linksy.util.createContentPickerForFragment
import com.emil.linksy.util.createImageFilePart
import com.emil.linksy.util.createVideoFilePart
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.showHint
import com.emil.linksy.util.string
import com.emil.linksy.util.trimMedia
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateMomentDialogFragment: DialogFragment()  {
    private lateinit var toolBar: MaterialToolbar
    private lateinit var addImage: ImageView
    private lateinit var addVideo: ImageView
    private lateinit var addAudio: ImageView
    private lateinit var contentImageView: ImageView
    private lateinit var deleteContent: ImageButton
    private lateinit var hintImageButton: ImageButton
    private lateinit var pickedVideoVideoView: VideoView
    private lateinit var publishButton: MaterialButton
    private lateinit var momentEditText: EditText
    private lateinit var playAudio:ImageView
    private lateinit var audioProgressBar: ProgressBar
    private lateinit var deleteAudio:ImageButton
    private lateinit var audioLinearLayout: LinearLayout
    private lateinit var mediaPlayerAudio: MediaPlayer
    private var isPlayingAudio = false
    private var imageUri: Uri? = null
    private var videoUri: Uri? = null
    private var audioUri: Uri? = null
    private val momentViewModel: MomentViewModel by viewModel<MomentViewModel>()
    private val tokenManager: TokenManager by inject()
    private var listener: AddMomentDialogListener? = null
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_moment_dialog, container, false)
        toolBar = view.findViewById(R.id.tb_moment)
        toolBar.setNavigationOnClickListener {
            dialog?.dismiss()
        }
        addImage=view.findViewById(R.id.iv_add_image)
        addVideo=view.findViewById(R.id.iv_add_video)
        addAudio=view.findViewById(R.id.iv_add_audio)
        contentImageView =view.findViewById(R.id.iv_content)
        momentEditText = view.findViewById(R.id.et_moment)
        publishButton = view.findViewById(R.id.bt_publish)
        deleteContent = view.findViewById(R.id.ib_delete_content)
        playAudio =  view.findViewById(R.id.iv_play_audio)
        audioProgressBar =  view.findViewById(R.id.pb_audio)
        deleteAudio = view.findViewById(R.id.ib_delete_audio)
        audioLinearLayout = view.findViewById(R.id.ll_audio)
        pickedVideoVideoView = view.findViewById(R.id.vv_picked_video)
        hintImageButton = view.findViewById(R.id.ib_hint)
        deleteContent.setOnClickListener {
            it.anim()
            imageUri = null
            videoUri=null
            contentImageView.hide()
            pickedVideoVideoView.hide()
            addImage.show()
            addVideo.show()
            it.hide()
            updatePublishButtonState()
        }

      hintImageButton.setOnClickListener {
            it.anim()
            it.showHint(requireContext(),R.string.moment_hint)
        }

        val pickImageLauncher = createContentPickerForFragment(this) { uri ->
            handleSelectedImage(uri)
            imageUri = uri
            updatePublishButtonState()
        }
        val pickVideoLauncher = createContentPickerForFragment(this) { uri ->
            handleSelectedVideo(uri)
            videoUri = uri
            updatePublishButtonState()
        }
        val pickAudioLauncher = createContentPickerForFragment(this) { uri ->
            handleSelectedAudio(uri)
            audioUri = uri
            updatePublishButtonState()
        }




        addImage.setOnClickListener {
            it.anim()
            pickImageLauncher.launch(ContentType.IMAGE.mimeType)
        }
        addVideo.setOnClickListener {
            it.anim()
            pickVideoLauncher.launch(ContentType.VIDEO.mimeType)
        }
        addAudio.setOnClickListener {
            it.anim()
            pickAudioLauncher.launch(ContentType.AUDIO.mimeType)
        }

        publishButton.setOnClickListener {
            it.anim()
            val loading = LoadingDialog(requireContext())
            loading.show()
            val text = momentEditText.string()
            val token = tokenManager.getAccessToken()

            val durationLimitSec = if (imageUri==null) Linksy.MOMENT_LONG_DURATION else Linksy.MOMENT_SHORT_DURATION


            val trimmedAudioUri = audioUri?.let { trimMedia(requireContext(), it, durationLimitSec) }
            val trimmedVideoUri = videoUri?.let { trimMedia(requireContext(), it,durationLimitSec) }

            val imagePart = imageUri?.let { createImageFilePart(requireContext(), it) }
            val videoPart = trimmedVideoUri?.let { createVideoFilePart(requireContext(), it) }
            val audioPart = trimmedAudioUri?.let { createAudioFilePart(requireContext(), it) }


           momentViewModel.createMoment(token,
                image = imagePart, video = videoPart,
                audio = audioPart,text =text,
                onSuccess = {
                    dialog?.dismiss()
                    loading.dismiss()
                })
        }

        return view
    }

    private fun handleSelectedAudio(uri: Uri) {
        addAudio.hide()
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
            addAudio.show()
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
    private fun handleSelectedImage(uri: Uri) {
        contentImageView.show()
        Glide.with(requireContext())
            .load(uri)
            .into(contentImageView)

       hideContentNavigation()
    }
    private fun handleSelectedVideo(uri: Uri) {
        pickedVideoVideoView.show()
        pickedVideoVideoView.setVideoURI(uri)
        pickedVideoVideoView.setOnPreparedListener { mp ->
            mp.setVolume(0f, 0f)
            pickedVideoVideoView.start()
        }
        pickedVideoVideoView.setOnCompletionListener {
            pickedVideoVideoView.start()
        }
        hideContentNavigation()
    }


    private fun hideContentNavigation (){
        addVideo.hide()
        addImage.hide()
        deleteContent.show()
    }
    private fun updatePublishButtonState() {
        publishButton.isEnabled = imageUri != null || videoUri != null
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onMomentAdded()
    }
    fun setAddMomentDialogListener(listener: AddMomentDialogListener) {
        this.listener = listener
    }

    interface AddMomentDialogListener {
        fun onMomentAdded()
    }
}