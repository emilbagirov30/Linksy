package com.emil.linksy.presentation.ui.navigation.channel

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.ChannelType
import com.emil.linksy.presentation.ui.LoadingDialog
import com.emil.linksy.presentation.viewmodel.ChannelViewModel
import com.emil.linksy.util.ContentType
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.createContentPickerForActivity
import com.emil.linksy.util.createImageFilePart
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.string
import com.emil.presentation.R
import com.emil.presentation.databinding.ActivityEditChannelBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditChannelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditChannelBinding
    private var imageUri: Uri? = null
    private var oldAvatarUrl:String? = ""
    private var oldName:String=""
    private val channelViewModel: ChannelViewModel by viewModel<ChannelViewModel>()
    private val tokenManager: TokenManager by inject()
    var channelType:ChannelType = ChannelType.PUBLIC
    private var oldLink:String? = null
    private var oldDescription:String=""
    private var oldType:ChannelType = ChannelType.PRIVATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditChannelBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val channelId = intent.getLongExtra(Linksy.INTENT_CHANNEL_ID_KEY, Linksy.DEFAULT_ID)
        val pickImageLauncher = createContentPickerForActivity(this) { uri ->
            handleSelectedImage(uri)
            imageUri = uri
        }
        binding.tb.setNavigationOnClickListener {
            finish()
        }

        ArrayAdapter.createFromResource(this, R.array.channel_type_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.sType.adapter = adapter
        }
        channelViewModel.getChannelManagementData(tokenManager.getAccessToken(),channelId, onSuccess = {
            binding.root.show() })


        binding.etName.addTextChangedListener{updateButtonState()}
        binding.etLink.addTextChangedListener{updateButtonState()}
        binding.etDescription.addTextChangedListener{updateButtonState()}


        channelViewModel.managementData.observe(this){mData ->
            oldAvatarUrl = mData.avatarUrl
             if (mData.avatarUrl!="null") {
                 Glide.with(this)
                     .load(Uri.parse(mData.avatarUrl))
                     .apply(RequestOptions.circleCropTransform())
                     .into(binding.ivChannelAvatar)
             }

             binding.etName.setText(mData.name)
             oldName = mData.name
             if (mData.description!=null) {
                 binding.etDescription.setText(mData.description)
                 oldDescription = mData.description!!
             }

             if (mData.link!=null) {
                 binding.etLink.setText(mData.link)
                 oldLink = mData.link
             }
             binding.sType.setSelection(if (mData.type == ChannelType.PUBLIC) 0 else 1)
             oldType = mData.type
        }



        binding.sType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                channelType = if (position == 0) ChannelType.PUBLIC else ChannelType.PRIVATE
                updateButtonState()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        binding.ibLoadAvatar.setOnClickListener {
            it.anim()
            pickImageLauncher.launch(ContentType.IMAGE.mimeType)
        }

        binding.btApply.setOnClickListener {
            val name =binding.etName.string()
            if (name.isEmpty()){
                binding.tvErrorNameShort.show()
            }else {
                binding.tvErrorNameShort.hide()
                val avatar = imageUri?.let { createImageFilePart(this, it) }
                val link = binding.etLink.string()
                val description = binding.etDescription.string()
                channelViewModel.createOrUpdateChannel(tokenManager.getAccessToken(),name,channelId,link,description,channelType,oldAvatarUrl,avatar, onSuccess = {
                        finish()
                })
            }
        }




    }
    private fun handleSelectedImage(uri: Uri) {
        oldAvatarUrl = null
        Glide.with(this)
            .load(uri)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.ivChannelAvatar)
        updateButtonState()
    }

private fun updateButtonState (){
    val name =binding.etName.string()
    val link = binding.etLink.string()
    val description = binding.etDescription.string()

    binding.btApply.isEnabled = oldAvatarUrl==null || oldType!=channelType || description!=oldDescription|| link!=oldLink || name!=oldName
}


}