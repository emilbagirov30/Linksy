package com.emil.linksy.presentation.ui.navigation.chat

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.linksy.adapters.UsersAdapter
import com.emil.linksy.presentation.viewmodel.ChatViewModel
import com.emil.linksy.presentation.viewmodel.PeopleViewModel
import com.emil.linksy.util.ContentType
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.createContentPickerForActivity
import com.emil.linksy.util.createContentPickerForFragment
import com.emil.linksy.util.createImageFilePart
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.showHint
import com.emil.linksy.util.string
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateGroupActivity : AppCompatActivity() {
    private lateinit var groupAvatarImageView:ImageView
    private lateinit var loadAvatarButton:ImageButton
    private lateinit var nameEditText: EditText
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var createButton: MaterialButton
    private lateinit var errorTextView: MaterialTextView
    private val peopleViewModel: PeopleViewModel by viewModel<PeopleViewModel>()
    private val chatViewModel: ChatViewModel by viewModel<ChatViewModel>()
    private val tokenManager: TokenManager by inject()
    private var imageUri: Uri? = null
    private lateinit var userAdapter: UsersAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        groupAvatarImageView = findViewById(R.id.iv_group_avatar)
        loadAvatarButton = findViewById(R.id.iv_load_avatar)
        nameEditText = findViewById(R.id.et_name)
        userRecyclerView = findViewById(R.id.rv_users)
        createButton = findViewById(R.id.bt_create)
        errorTextView = findViewById(R.id.tv_error_name_short)
        val toolBar = findViewById<MaterialToolbar>(R.id.tb)
        toolBar.setNavigationOnClickListener {
            finish()
        }
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        peopleViewModel.userList.observe(this){ userlist ->
            userAdapter = UsersAdapter(
                userList = userlist,
                context = this,
                isNeedChoice = true
            )
            userRecyclerView.adapter = userAdapter
        }
        peopleViewModel.getUserSubscriptions(tokenManager.getAccessToken())

        val pickImageLauncher = createContentPickerForActivity(this) { uri ->
            handleSelectedImage(uri)
            imageUri = uri

        }
       loadAvatarButton.setOnClickListener {
            it.anim()
            pickImageLauncher.launch(ContentType.IMAGE.mimeType)
        }
        createButton.setOnClickListener {
            val name = nameEditText.string()
            if (name.isEmpty()){
                 errorTextView.show()
            }else{
                errorTextView.hide()
                val selectedUsers = userAdapter.getSelectedUserIds()
                val idsAsString = selectedUsers.joinToString(separator = ",")
                val avatar = imageUri?.let { createImageFilePart(this, it) }
                chatViewModel.createGroup(tokenManager.getAccessToken(),idsAsString,name,avatar, onSuccess = {finish()})
            }

        }

    }
    private fun handleSelectedImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .apply(RequestOptions.circleCropTransform())
            .into(groupAvatarImageView)
    }
}