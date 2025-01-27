package com.emil.linksy.presentation.ui.navigation.channel

import android.annotation.SuppressLint
import android.content.DialogInterface
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
import java.util.ArrayList

class AddChannelPostDialogFragment: DialogFragment() {
    private  var channelId: Long = -1
    private  var postId: Long? = null
    private var text:String? = null
    private var imageUrl:String? = null
    private var videoUrl:String? = null
    private var audioUrl:String? = null
    private var isEdit:Boolean? = null
    private var title:String? = null
    private var listener: AddChannelPostDialogListener? = null
    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val EDIT = "EDIT"
        private const val TEXT = "TEXT"
        private const val URL_IMAGE = "IMAGE"
        private const val URL_VIDEO = "VIDEO"
        private const val URL_AUDIO = "AUDIO"
        private const val POLL_TITLE = "POLL_TITLE"
        private const val POST_ID = "POST_ID"
        private const val OPTIONS = "OPTIONS"
        fun newInstance(channelId: Long,postId:Long=-1,isEdit:Boolean = false,text:String?=null,imageUrl:String?=null,
                        videoUrl:String?=null,audioUrl:String?=null,title:String?=null,options:List<String>? = emptyList()
        ): AddChannelPostDialogFragment {
            val fragment = AddChannelPostDialogFragment()
            val args = Bundle()
            args.putLong(CHANNEL_ID, channelId)
            args.putLong(POST_ID, postId)
            args.putBoolean(EDIT, isEdit)
            args.putString(TEXT,text)
            args.putString(URL_IMAGE,imageUrl)
            args.putString(URL_VIDEO,videoUrl)
            args.putString(URL_AUDIO,audioUrl)
            args.putString(POLL_TITLE, title)
            if (options!=null)
            args.putStringArrayList(OPTIONS,  ArrayList(options))
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        arguments?.let {
            channelId = it.getLong(CHANNEL_ID)
            isEdit = arguments?.getBoolean(EDIT)
            text = arguments?.getString(TEXT)
            imageUrl = arguments?.getString(URL_IMAGE)
            videoUrl = arguments?.getString(URL_VIDEO)
            audioUrl = arguments?.getString(URL_AUDIO)
            title = arguments?.getString(POLL_TITLE)
            if (arguments?.getStringArrayList(OPTIONS)!=null)
            optionlist = arguments?.getStringArrayList(OPTIONS)!!.toMutableList()
            postId = arguments?.getLong(POST_ID)  }

    }


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

       if (isEdit==true) {
            binding.tvResetPollInfo.show()
            binding.tvTitle.text = getString(R.string.edit)

            text?.let {   binding.etPost.setText(it)}

            imageUrl?.let {
                handleSelectedImage(Uri.parse(it))
            }

            videoUrl?.let {
                handleSelectedVideo(Uri.parse(it))
            }

            audioUrl?.let {
                handleSelectedAudio(Uri.parse(it))
            }

           title?.let {
                addPollInPost(it,optionlist)
            }

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
        binding.btAddPoll.setOnClickListener {
            it.anim()
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
            channelViewModel.publishPost(token,channelId,text,postId,imageUrl,videoUrl,audioUrl,imagePart,videoPart,audioPart,pollTitle,optionlist.toList(),
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
             imageUrl = null
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
             videoUrl = null
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
                audioUrl = null
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


    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onPostAdded()
    }
    fun setAddPostDialogListener(listener: AddChannelPostDialogListener) {
        this.listener = listener
    }
    interface AddChannelPostDialogListener {
        fun onPostAdded()
    }
}