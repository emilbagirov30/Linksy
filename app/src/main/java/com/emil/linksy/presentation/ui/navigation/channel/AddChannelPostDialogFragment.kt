package com.emil.linksy.presentation.ui.navigation.channel

import android.annotation.SuppressLint
import android.app.Dialog
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.emil.linksy.presentation.ui.LoadingDialog
import com.emil.linksy.presentation.viewmodel.ChannelViewModel
import com.emil.linksy.util.ContentType
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.createAudioFilePart
import com.emil.linksy.util.createContentPickerForFragment
import com.emil.linksy.util.createImageFilePart
import com.emil.linksy.util.createVideoFilePart
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.string
import com.emil.presentation.R
import com.emil.presentation.databinding.AddChannelPostDialogBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddChannelPostDialogFragment: DialogFragment() {
    private  var channelId: Long = -1

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"

        fun newInstance(channelId: Long): AddChannelPostDialogFragment {
            val fragment = AddChannelPostDialogFragment()
            val args = Bundle()
            args.putLong(CHANNEL_ID, channelId)
            fragment.arguments = args
            return fragment
        }

    }

    private lateinit var binding: AddChannelPostDialogBinding
    private var pollTitle:String = ""
    private var optionlist:MutableList<String> = mutableListOf()
    private var imageUri: Uri? = null
    private var videoUri: Uri? = null
    private var audioUri: Uri? = null
    private lateinit var mediaPlayerAudio: MediaPlayer
    private var isPlayingAudio = false
    private val tokenManager: TokenManager by inject()
    private val channelViewModel:ChannelViewModel by viewModel<ChannelViewModel>()



    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = AddChannelPostDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tb.setNavigationOnClickListener {
            dismiss()
        }
        binding.etPost.addTextChangedListener { updatePublishButtonState() }

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
        binding.btAddPoll.setOnClickListener {
            AddPollDialogFragment(this).show(parentFragmentManager,"AddPollDialogFragment")
        }

        binding.btAddPicture.setOnClickListener {
            it.anim()
            pickImageLauncher.launch(ContentType.IMAGE.mimeType)
        }

        binding.btAddVideo.setOnClickListener {
            it.anim()
            pickVideoLauncher.launch(ContentType.VIDEO.mimeType)
        }


        binding.btAddAudio.setOnClickListener {
            it.anim()
            pickAudioLauncher.launch(ContentType.AUDIO.mimeType)
        }
        binding.btPublish.setOnClickListener { it ->
            it.anim()
            val loading = LoadingDialog(requireContext())
            loading.show()
            val text = binding.etPost.string()
            val token = tokenManager.getAccessToken()
            val imagePart = imageUri?.let { createImageFilePart(requireContext(), it) }
            val videoPart = videoUri?.let { createVideoFilePart(requireContext(), it) }
            val audioPart = audioUri?.let { createAudioFilePart(requireContext(), it) }
            channelViewModel.publishPost(token,channelId,text,imagePart,videoPart,audioPart,pollTitle,optionlist.toList(),
                onSuccess = {
                    loading.dismiss()
                    dismiss()})
        }
    }

    private fun handleSelectedImage(uri: Uri) {
        binding.llPickedContent.show()
        binding.flPicture.show()
        Glide.with(requireContext())
            .load(uri)
            .into(binding.ivPickedPicture)

        binding.ibDeletePicture.ibDelete.setOnClickListener {
            it.anim()
            binding.flPicture.hide()
            if (!binding.flPicture.isVisible||!binding.flPoll.isVisible)  binding.llPickedContent.hide()
            imageUri = null
            updatePublishButtonState()
        }
    }

    private fun handleSelectedVideo(uri: Uri) {
        binding.llPickedContent.show()
       binding.flVideo.show()
       binding.vvPickedVideo.setVideoURI(uri)
        binding.vvPickedVideo.setOnPreparedListener { mp ->
            mp.setVolume(0f, 0f)
            binding.vvPickedVideo.start()
        }
        binding.vvPickedVideo.setOnCompletionListener {
            binding.vvPickedVideo.start()
        }
       binding.ibDeleteVideo.ibDelete.setOnClickListener {
            it.anim()
           binding.vvPickedVideo.stopPlayback()
            binding.flVideo.hide()
            if (!binding.flPicture.isVisible||!binding.flPoll.isVisible)  binding.llPickedContent.hide()
            videoUri = null
            updatePublishButtonState()
        }
    }
    private fun handleSelectedAudio(uri: Uri) {

        binding.llPickedAudio.show()
        binding.ivPlayAudio.setImageResource(R.drawable.ic_play)
        mediaPlayerAudio = MediaPlayer().apply {
            setDataSource(requireContext(), uri)
            prepareAsync()
            setOnPreparedListener {
                isPlayingAudio = false
               binding.pbPickedAudio.progress = 0
                binding.pbPickedAudio.max = it.duration

            }
            setOnCompletionListener {
                isPlayingAudio = false
                binding.pbPickedAudio.progress = 0
                binding.ivPlayAudio.setImageResource(R.drawable.ic_play)
            }}
            binding.ivPlayAudio.setOnClickListener {
                if (isPlayingAudio) {
                    mediaPlayerAudio.pause()
                    binding.ivPlayAudio.setImageResource(R.drawable.ic_play)
                    isPlayingAudio = false
                } else {
                    mediaPlayerAudio.start()
                    binding.ivPlayAudio.setImageResource(R.drawable.ic_pause)
                    isPlayingAudio = true
                    startProgressAudioUpdate()
                }
            }


            binding.ibDeleteAudio.ibDelete.setOnClickListener {
                it.anim()
                mediaPlayerAudio.stop()
                mediaPlayerAudio.reset()
                isPlayingAudio = false
                binding.pbPickedAudio.progress = 0
                binding.llPickedAudio.hide()
                audioUri = null
                updatePublishButtonState()
            }
        }
        private fun startProgressAudioUpdate() {

            CoroutineScope(Dispatchers.Main).launch {
                while (mediaPlayerAudio.isPlaying) {
                    val currentPosition = mediaPlayerAudio.currentPosition
                    val totalDuration = mediaPlayerAudio.duration
                    binding.pbPickedAudio.max = totalDuration
                    binding.pbPickedAudio.progress = currentPosition
                    delay(5)
                }
            }
        }
    fun addPollInPost(title:String,options:MutableList<String>){
        binding.llPickedContent.show()
        binding.flPoll.show()
        pollTitle = title
        optionlist =  options

        binding.ibDeletePoll.ibDelete.setOnClickListener {
            binding.flPoll.hide()
            pollTitle = ""
            optionlist.clear()
            if (!binding.flPicture.isVisible||!binding.flVideo.isVisible)  binding.llPickedContent.hide()
        }
        updatePublishButtonState()
    }
       private fun updatePublishButtonState() {
            val isMediaSelected = imageUri != null || videoUri != null || audioUri != null
             val isPollAdded = optionlist.isNotEmpty()
            val isTextNotEmpty = binding.etPost.string().isNotEmpty()
           binding.btPublish.isEnabled = isMediaSelected || isTextNotEmpty || isPollAdded
        }


    override fun getTheme() = R.style.FullScreenDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        arguments?.let {
            channelId = it.getLong(CHANNEL_ID)
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }

}