package com.emil.linksy.presentation.ui.navigation.channel

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.linksy.presentation.viewmodel.ChannelViewModel
import com.emil.linksy.util.ContentType
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.createContentPickerForActivity
import com.emil.linksy.util.createImageFilePart
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.string
import com.emil.presentation.R
import com.emil.presentation.databinding.ActivityCreateChannelBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreateChannelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateChannelBinding
    private val channelViewModel:ChannelViewModel by viewModel<ChannelViewModel>()
    private val tokenManager: TokenManager by inject()
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateChannelBinding.inflate(layoutInflater)
        val view = binding.root


        val pickImageLauncher = createContentPickerForActivity(this) { uri ->
            handleSelectedImage(uri)
            imageUri = uri
        }
        binding.ibLoadAvatar.setOnClickListener {
            it.anim()
            pickImageLauncher.launch(ContentType.IMAGE.mimeType)
        }
        var channelType = "PUBLIC"
        binding.sType.setSelection(0)

            ArrayAdapter.createFromResource(this, R.array.channel_type_array, android.R.layout.simple_spinner_item).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.sType.adapter = adapter
            }
            binding.sType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    channelType = if (position == 0) "PUBLIC" else "PRIVATE"
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.btCreate.setOnClickListener {
            val name =binding.etName.string()
            if (name.isEmpty()){
                binding.tvErrorNameShort.show()
            }else{
                binding.tvErrorNameShort.hide()
                val avatar = imageUri?.let { createImageFilePart(this, it) }
                val link = binding.etLink.string()
                val description = binding.etDescription.string()
                val type = channelType
                channelViewModel.createChannel(tokenManager.getAccessToken(),name,link,description,type,avatar, onSuccess = {finish()})

            }
        }
        setContentView(view)

     }
    private fun handleSelectedImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.ivChannelAvatar)
    }

    }
