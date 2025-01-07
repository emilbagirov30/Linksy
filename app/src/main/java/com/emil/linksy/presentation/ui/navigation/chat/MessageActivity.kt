package com.emil.linksy.presentation.ui.navigation.chat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emil.linksy.presentation.custom_view.CustomAudioWave
import com.emil.linksy.util.AudioRecorderManager
import com.emil.linksy.util.ContentType
import com.emil.linksy.util.anim
import com.emil.linksy.util.createContentPickerForActivity
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MessageActivity : AppCompatActivity() {
    private lateinit var avatarImageView: ImageView
    private lateinit var titleTextView: MaterialTextView
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var recordButton: ImageButton
    private lateinit var attachImageButton: ImageButton
    private lateinit var pickedPictureImageView:ImageView
    private lateinit var pickedVideoVideoView: VideoView
    private lateinit var deletePictureButton:ImageButton
    private lateinit var deleteVideoButton:ImageButton
    private lateinit var playAudio:ImageView
    private lateinit var audioProgressBar: ProgressBar
    private lateinit var deleteAudio:ImageButton
    private lateinit var mediaLinearLayout: LinearLayout
    private lateinit var  pictureFrameLayout: FrameLayout
    private lateinit var videoFrameLayout: FrameLayout
    private lateinit var audioLinearLayout: LinearLayout
    private lateinit var mediaPlayerAudio: MediaPlayer
    private lateinit var pickImageLauncher:ActivityResultLauncher<String>
    private lateinit var pickVideoLauncher:ActivityResultLauncher<String>
    private lateinit var pickAudioLauncher:ActivityResultLauncher<String>
    private var isPlayingAudio = false
    private var imageUri: Uri? = null
    private var videoUri: Uri? = null
    private var audioUri: Uri? = null
    private var voiceUri: Uri? = null
    private var job: Job? = null
    private var secondsElapsed = 0
    private val RECORD_AUDIO_PERMISSION_CODE = 100
    private lateinit var audioRecorderManager: AudioRecorderManager
    private lateinit var audioWaveView: CustomAudioWave
    private lateinit var voiceLinearLayout: LinearLayout
    private lateinit var stopWatchTextView: MaterialTextView
    private lateinit var deleteVoice:ImageButton
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        avatarImageView = findViewById(R.id.iv_avatar)
        titleTextView = findViewById(R.id.tv_title)
        chatRecyclerView = findViewById(R.id.rv_chat)
        messageEditText = findViewById(R.id.et_message)
        attachImageButton = findViewById(R.id.ib_attach)
        sendButton = findViewById(R.id.ib_send)
        recordButton = findViewById(R.id.ib_record)
        pickedPictureImageView = findViewById(R.id.iv_picked_picture)
        pickedVideoVideoView = findViewById(R.id.vv_picked_video)
        deletePictureButton =findViewById(R.id.ib_delete_picture)
        deleteVideoButton = findViewById(R.id.ib_delete_video)
        playAudio =  findViewById(R.id.iv_play_audio)
        audioProgressBar = findViewById(R.id.pb_picked_audio)
        deleteAudio = findViewById(R.id.ib_delete_audio)
        mediaLinearLayout = findViewById(R.id.ll_picked_media)
        audioLinearLayout = findViewById(R.id.ll_picked_audio)
        pictureFrameLayout =  findViewById(R.id.fl_picture)
        videoFrameLayout =  findViewById(R.id.fl_video)
        audioRecorderManager = AudioRecorderManager(this)
        audioWaveView = findViewById(R.id.caw_voice)
        voiceLinearLayout = findViewById(R.id.ll_voice)
        stopWatchTextView = findViewById(R.id.tv_stopwatch)
        deleteVoice = findViewById(R.id.ib_delete_voice)
        val toolBar = findViewById<MaterialToolbar>(R.id.tb)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val currentText = s.toString().trim()
                if (currentText.isNotEmpty()) {
                    sendButton.show()
                    recordButton.hide()
                } else {
                    sendButton.hide()
                    recordButton.show()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }

        messageEditText.addTextChangedListener(textWatcher)


messageEditText.setOnClickListener {
    it.anim()
}


            recordButton.setOnClickListener {
                   it.anim()
                audioWaveView.show()
                if ( checkAudioPermission()) {
                    startRecording()
                    sendButton.show()
                    recordButton.hide()
                    voiceLinearLayout.show()
                    updateStopwatch()
                }
            }


         deleteVoice.setOnClickListener {
             audioWaveView.hide()
             sendButton.hide()
             recordButton.show()
             voiceLinearLayout.hide()
             secondsElapsed=0
             audioRecorderManager.stopRecording()
             job?.cancel()
         }




        toolBar.setNavigationOnClickListener {
            finish()
        }
          pickImageLauncher = createContentPickerForActivity(this) { uri ->
            handleSelectedImage(uri)
            imageUri = uri
        }
          pickVideoLauncher =  createContentPickerForActivity(this) { uri ->
            handleSelectedVideo(uri)
            videoUri = uri
        }
          pickAudioLauncher =  createContentPickerForActivity(this) { uri ->
            handleSelectedAudio(uri)
            audioUri = uri
        }
        attachImageButton.setOnClickListener {
            showPopup(it)
        }
    }


    private fun handleSelectedImage(uri: Uri) {
        mediaLinearLayout.show()
        pictureFrameLayout.show()
        Glide.with(this)
            .load(uri)
            .into(pickedPictureImageView)

        deletePictureButton.setOnClickListener {
            it.anim()
            pictureFrameLayout.hide()
            if (!videoFrameLayout.isVisible) mediaLinearLayout.hide()
            imageUri = null
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
        }
    }


    private fun handleSelectedAudio(uri: Uri) {

        audioLinearLayout.show()
        playAudio.setImageResource(R.drawable.ic_play)
        mediaPlayerAudio = MediaPlayer().apply {
            setDataSource(this@MessageActivity, uri)
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




    @SuppressLint("InflateParams")
    private fun showPopup(anchor: View) {
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.popup_attach, null)

        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val popupHeight = popupView.measuredHeight


        val xOffset = 0
        val yOffset = -popupHeight - anchor.height - 10
        popupWindow.showAsDropDown(anchor, xOffset, yOffset)

        val addImageButton = popupView.findViewById<ImageButton>(R.id.ib_add_image)
        val addVideoButton = popupView.findViewById<ImageButton>(R.id.ib_add_video)
        val addAudioButton = popupView.findViewById<ImageButton>(R.id.ib_add_audio)

        addImageButton.setOnClickListener {
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
    }




    fun updateStopwatch () {
        secondsElapsed++
        val minutes = secondsElapsed / 60
        val seconds = secondsElapsed % 60
        stopWatchTextView.text = String.format("%02d:%02d", minutes, seconds)
        job = CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            updateStopwatch()
        }
    }


    private fun checkAudioPermission(): Boolean {

        val readPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val writePermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val recordAudioPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (readPermission && writePermission && recordAudioPermission) {
            return true
        }
        ActivityCompat.requestPermissions(
           this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
            ),
            RECORD_AUDIO_PERMISSION_CODE
        )

        return false
    }
    private fun startRecording() {
        audioRecorderManager.startRecording { amplitude ->
              runOnUiThread {
                audioWaveView.addAmplitude(amplitude)
            }
        }
    }




}