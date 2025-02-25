package com.emil.linksy.presentation.ui.navigation.chat

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.MessageStatus
import com.emil.linksy.adapters.MessagesAdapter
import com.emil.linksy.presentation.custom_view.CustomAudioWave
import com.emil.linksy.presentation.ui.page.UserPageActivity
import com.emil.linksy.presentation.viewmodel.ChatViewModel
import com.emil.linksy.presentation.viewmodel.MessageViewModel
import com.emil.linksy.util.AudioRecorderManager
import com.emil.linksy.util.ContentType
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.createAudioFilePart
import com.emil.linksy.util.createContentPickerForActivity
import com.emil.linksy.util.createImageFilePart
import com.emil.linksy.util.createVideoFilePart
import com.emil.linksy.util.createVoiceFilePart
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.showToast
import com.emil.linksy.util.string
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MessageActivity : AppCompatActivity() {
    private lateinit var avatarImageView: ImageView
    private lateinit var titleTextView: MaterialTextView
    private lateinit var memberCountTextView: MaterialTextView
    private lateinit var messageRecyclerView: RecyclerView
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
    private var isRecording = false
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
    private val messageViewModel: MessageViewModel by viewModel<MessageViewModel>()
    private val chatViewModel:ChatViewModel by viewModel<ChatViewModel>()
    private val tokenManager: TokenManager by inject()
    private var userId:Long = -1
    private var chatId:Long = -100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        avatarImageView = findViewById(R.id.iv_avatar)
        titleTextView = findViewById(R.id.tv_title)
        memberCountTextView = findViewById(R.id.tv_member_count)
        messageRecyclerView = findViewById(R.id.rv_message)
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
        val confirmedImageView = findViewById<ImageView>(R.id.iv_confirmed)
        val statusTextView = findViewById<MaterialTextView>(R.id.tv_status)
        val downButton = findViewById<ImageButton>(R.id.ib_down)
        val toolBar = findViewById<MaterialToolbar>(R.id.tb)
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        val sharedPref: SharedPreferences = getSharedPreferences("appData", Context.MODE_PRIVATE)
        userId = sharedPref.getLong("ID",-1)
        toolBar.setNavigationOnClickListener {
            finish()
        }
        val recipientId: Long? = if (intent.hasExtra("USER_ID")) {
            intent.getLongExtra("USER_ID", -1L)
        } else null
        val avatarUrl = intent.getStringExtra("AVATAR_URL")
        val title = intent.getStringExtra("NAME")
         chatId = if (intent.hasExtra("CHAT_ID")) {
            intent.getLongExtra("CHAT_ID", -1L)
        } else -100
        val confirmed = intent.getBooleanExtra("CONFIRMED",false)
        val isGroup = intent.getBooleanExtra("ISGROUP",false)
        if(confirmed) confirmedImageView.show() else confirmedImageView.hide()
        if(isGroup) {
            if (avatarUrl == "null") avatarImageView.setImageResource(R.drawable.default_group_avatar)
            memberCountTextView.show()
            chatViewModel.getGroupMembers(tokenManager.getAccessToken(), chatId)
            chatViewModel.getGroupSenders(tokenManager.getAccessToken(), chatId, onError = {
                showToast(this,R.string.error_loading_data)
            })

            chatViewModel.sendersList.observe(this){sl->
                messageViewModel.messageList.observe(this) { messageList ->
                    messageRecyclerView.adapter = MessagesAdapter(messageList, this, userId, chatSensersList = sl,messageViewModel,tokenManager)
                    viewMessage(chatId)
                }
            }
            chatViewModel.memberList.observe(this) { ml ->
                memberCountTextView.text = "${ml.size} ${getString(R.string.members)}"
                messageViewModel.getUserMessagesByChat(tokenManager.getAccessToken(), chatId,
                    onSuccess = {
                        subscribeToUpdates(chatId)
                    })
            }

        }   else  {

            if(chatId == -100L) {
                chatViewModel.getChatId(tokenManager.getAccessToken(),recipientId!!)
                chatViewModel.chatId.observe(this){id ->
                    chatId=id
                    messageViewModel.getUserMessagesByChat(tokenManager.getAccessToken(),id, onSuccess = {
                        subscribeToUpdates(id)}, onError = {
                        getMessagesFromLocalDatabase(id)
                    })
                }
            }
            else {
                messageViewModel.getUserMessagesByChat(tokenManager.getAccessToken(),chatId, onSuccess = {
                    subscribeToUpdates(chatId) }, onError = {
                   getMessagesFromLocalDatabase(chatId)
                })
            }
                messageViewModel.messageList.observe(this){messageList ->
                messageRecyclerView.adapter = MessagesAdapter(messageList, this, userId, messageViewModel = messageViewModel, tokenManager = tokenManager)
                    if(messageList.isNotEmpty())
                    viewMessage(messageList[0].chatId)

                    messageRecyclerView.scrollToPosition(messageList.size-1)
            }

        }
                    messageViewModel.status.observe(this){status ->
                    when(status.status){
                        MessageStatus.TEXT -> statusTextView.text = "${status.name} ${getString(R.string.writes)}.."
                        MessageStatus.VOICE -> statusTextView.text = "${status.name} ${getString(R.string.recording_voice)}.."
                        MessageStatus.IMAGE -> statusTextView.text = "${status.name} ${getString(R.string.select_image)}.."
                        MessageStatus.NOTHING -> statusTextView.text = ""

                }}

        if (avatarUrl!="null"){
            Glide.with(this)
                .load(avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(avatarImageView)
        }
        titleTextView.text = title
        avatarImageView.setOnClickListener {
    if(!isGroup) {
        val switchingToUserPageActivity =
            Intent(this, UserPageActivity()::class.java)
        switchingToUserPageActivity.putExtra("USER_ID", recipientId)
        startActivity(switchingToUserPageActivity)
    }else{
        val switchingToGroupMemberActivity =
            Intent(this, GroupActivity::class.java)
        switchingToGroupMemberActivity.putExtra("GROUP_ID", chatId)
        startActivity(switchingToGroupMemberActivity)
    }
}

        val typingHandler = Handler(Looper.getMainLooper())
        val typingDelay = 1500L
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val currentText = s.toString().trim()
                if (currentText.isNotEmpty()) {
                    sendButton.show()
                    recordButton.hide()
                    messageViewModel.sendStatus(chatId,userId,MessageStatus.TEXT)
                    typingHandler.removeCallbacksAndMessages(null)
                } else {
                    sendButton.hide()
                    recordButton.show()

                }
            }
            override fun afterTextChanged(p0: Editable?) {
                typingHandler.postDelayed({
                    messageViewModel.sendStatus(chatId, userId, MessageStatus.NOTHING)
                }, typingDelay)
            }
        }
            messageEditText.addTextChangedListener(textWatcher)

            sendButton.setOnClickListener { view->
                 view.anim()
                 if (isRecording){
                     stopRecording()
                     val recordFile = audioRecorderManager.getRecordedFile()
                     voiceUri = Uri.fromFile(recordFile)
                 }
                val text = messageEditText.string()
                val imagePart = imageUri?.let { createImageFilePart(this, it) }
                val videoPart = videoUri?.let { createVideoFilePart(this, it) }
                val audioPart = audioUri?.let { createAudioFilePart(this, it) }
                val voicePart = voiceUri?.let { createVoiceFilePart(this, it) }
                messageViewModel.sendMessage(tokenManager.getAccessToken(),recipientId,chatId,text,imagePart,videoPart,audioPart,voicePart,
                    onSuccess = { clear() })
            }

            recordButton.setOnClickListener {
                it.anim()
                if (checkAudioPermission()) startRecording()

            }

         deleteVoice.setOnClickListener { stopRecording() }

          pickImageLauncher = createContentPickerForActivity(this) { uri ->
              handleSelectedImage(uri)
              imageUri = uri
              sendButton.show()
              recordButton.hide()

        }
          pickVideoLauncher =  createContentPickerForActivity(this) { uri ->
              handleSelectedVideo(uri)
              videoUri = uri
              sendButton.show()
              recordButton.hide()
        }
          pickAudioLauncher =  createContentPickerForActivity(this) { uri ->
              handleSelectedAudio(uri)
              audioUri = uri
              sendButton.show()
              recordButton.hide()

        }

        attachImageButton.setOnClickListener { showPopup(it)}
        messageRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled( chatList: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled( chatList, dx, dy)
                val layoutManager =  chatList.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                if (lastVisibleItemPosition == totalItemCount - 1) downButton.hide()
                else  downButton.show()
            }
        })
        downButton.setOnClickListener {
            it.anim()
            down()
        }

    }
    private fun down(){
        val adapter = messageRecyclerView.adapter as MessagesAdapter
        val layoutManager = messageRecyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(adapter.itemCount - 1, 0)
    }


    private fun getMessagesFromLocalDatabase (chatId:Long){
        messageViewModel.getUserMessagesByChatFromLocalDb(chatId)
        showToast(this,R.string.loaded_from_cache)
    }

    private fun subscribeToUpdates (chatId:Long){
        messageViewModel.subscribeToUserMessages(tokenManager.getWsToken(),chatId)
        messageViewModel.subscribeToViewed(tokenManager.getWsToken(),chatId)
        messageViewModel.subscribeToDeleted(tokenManager.getWsToken(),chatId)
        messageViewModel.subscribeToEdited(tokenManager.getWsToken(),chatId)
        messageViewModel.subscribeToStatus(tokenManager.getWsToken(),chatId)
    }

    private fun viewMessage (chatId: Long){
        messageViewModel.viewed(tokenManager.getAccessToken(),chatId)
    }


    private fun handleSelectedImage(uri: Uri) {
        mediaLinearLayout.show()
        pictureFrameLayout.show()
        Glide.with(this)
            .load(uri)
            .into(pickedPictureImageView)
        messageViewModel.sendStatus(chatId,userId,MessageStatus.NOTHING)
        deletePictureButton.setOnClickListener {
            it.anim()
            pictureFrameLayout.hide()
            if (!videoFrameLayout.isVisible) {
                mediaLinearLayout.hide()
                sendButton.hide()
                recordButton.show()
            }
            pictureFrameLayout.hide()
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
            if (!pictureFrameLayout.isVisible) {
                mediaLinearLayout.hide()
                sendButton.hide()
                recordButton.show()
            }
            videoUri = null
        }
    }


    private fun handleSelectedAudio(uri: Uri) {
        messageViewModel.sendStatus(chatId,userId,MessageStatus.NOTHING)
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
            if(!mediaLinearLayout.isVisible) {
                sendButton.hide()
                recordButton.show()
            }
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
            messageViewModel.sendStatus(chatId,userId,MessageStatus.IMAGE)
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
    @SuppressLint("DefaultLocale")
    private fun updateStopwatch () {
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
        audioWaveView.show()
        isRecording = true
        sendButton.show()
        recordButton.hide()
        voiceLinearLayout.show()
        updateStopwatch()
        audioRecorderManager.startRecording { amplitude ->
              runOnUiThread {
                audioWaveView.addAmplitude(amplitude)
            }
        }
        messageViewModel.sendStatus(chatId,userId,MessageStatus.VOICE)
    }


    private fun stopRecording() {
        audioWaveView.hide()
        sendButton.hide()
        recordButton.show()
        voiceLinearLayout.hide()
        secondsElapsed=0
        audioRecorderManager.stopRecording()
        job?.cancel()
        isRecording = false
        messageViewModel.sendStatus(chatId,userId,MessageStatus.NOTHING)
    }
    private fun clear() {
        sendButton.hide()
        recordButton.show()
        voiceLinearLayout.hide()
        mediaLinearLayout.hide()
        videoFrameLayout.hide()
        pictureFrameLayout.hide()
        secondsElapsed=0
        messageEditText.setText("")
        audioWaveView.hide()
        imageUri = null
        videoUri = null
        audioUri = null
        voiceUri = null
        messageViewModel.sendStatus(chatId,userId,MessageStatus.NOTHING)
    }


}