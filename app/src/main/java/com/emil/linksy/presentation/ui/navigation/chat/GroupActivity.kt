package com.emil.linksy.presentation.ui.navigation.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.linksy.adapters.UsersAdapter
import com.emil.linksy.presentation.ui.BigPictureDialog
import com.emil.linksy.presentation.ui.navigation.MainNavigationActivity
import com.emil.linksy.presentation.ui.navigation.people.RelationsDialogFragment
import com.emil.linksy.presentation.viewmodel.ChatViewModel
import com.emil.linksy.util.ContentType
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.RelationType
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.createContentPickerForActivity
import com.emil.linksy.util.createImageFilePart
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.string
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class GroupActivity : AppCompatActivity() {
    private val chatViewModel: ChatViewModel by viewModel<ChatViewModel>()
    private val tokenManager: TokenManager by inject()
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var  groupAvatarImageView:ImageView
    private lateinit var  loadAvatarButton:ImageButton
    private lateinit var  nameEditText:EditText
    private lateinit var  applyButton:MaterialButton
    private var imageUri: Uri? = null
    private lateinit var errorTextView: MaterialTextView
    private  var oldAvatarUrl:String? = null
    private var oldName:String=""
    private var groupId:Long = -100L
    private var membersIdList: List<Long> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        val toolBar = findViewById<MaterialToolbar>(R.id.tb)
        groupAvatarImageView = findViewById(R.id.iv_group_avatar)
        loadAvatarButton = findViewById(R.id.iv_load_avatar)
        nameEditText = findViewById(R.id.et_name)
        applyButton = findViewById(R.id.bt_apply)
        val leaveButton = findViewById<MaterialButton>(R.id.bt_leave)
        val addMembersButton = findViewById<MaterialButton>(R.id.bt_add_members)
        userRecyclerView = findViewById(R.id.rv_users)
        errorTextView = findViewById(R.id.tv_error_name_short)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        groupId = intent.getLongExtra("GROUP_ID", -1)

        val pickImageLauncher = createContentPickerForActivity(this) { uri ->
            handleSelectedImage(uri)
            imageUri = uri

        }
        addMembersButton.setOnClickListener {
            RelationsDialogFragment.newInstance(RelationType.ADD_MEMBERS,groupId = groupId, memberIdList = membersIdList).show(
                supportFragmentManager, "RelationsDialogFragment"
            )
        }

         leaveButton.setOnClickListener {
             chatViewModel.leave(tokenManager.getAccessToken(),groupId, onSuccess = {
                 val intent = Intent(this, MainNavigationActivity::class.java)
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                 startActivity(intent)
             })
         }
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
              updateButtonState()
            }

            override fun afterTextChanged(p0: Editable?) {}

        }
        nameEditText.addTextChangedListener(textWatcher)
        chatViewModel.getGroupData(tokenManager.getAccessToken(),groupId)
        chatViewModel.groupData.observe(this){data ->
            oldName = data.name
            oldAvatarUrl = data.avatarUrl
            if (data.avatarUrl!= Linksy.EMPTY_AVATAR){
                Glide.with(this)
                    .load(data.avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(groupAvatarImageView)

                groupAvatarImageView.setOnClickListener {
                    BigPictureDialog.newInstance(data.avatarUrl).show(supportFragmentManager,  "BigPictureDialog")
                }
            }
            nameEditText.setText(data.name)
        }

        loadAvatarButton.setOnClickListener {
            it.anim()
            pickImageLauncher.launch(ContentType.IMAGE.mimeType)
        }



        applyButton.setOnClickListener {
            val name = nameEditText.string()
            if (name.isEmpty()){
                errorTextView.show()
            }else{
                errorTextView.hide()
                val avatar = imageUri?.let { createImageFilePart(this, it) }
                chatViewModel.editGroup(tokenManager.getAccessToken(),groupId, name, oldAvatarUrl, avatar, onSuccess = {
                    finish()
                })
            }

        }

        chatViewModel.memberList.observe(this){ memberList ->
            membersIdList = memberList.map { it.id }
            userRecyclerView.adapter =
                UsersAdapter(userList = memberList.toMutableList(),context = this)
        }
        chatViewModel.getGroupMembers(tokenManager.getAccessToken(),groupId)

        toolBar.setNavigationOnClickListener {
            finish()
        }

    }
    private fun handleSelectedImage(uri: Uri) {
        oldAvatarUrl = null
        Glide.with(this)
            .load(uri)
            .apply(RequestOptions.circleCropTransform())
            .into(groupAvatarImageView)
        updateButtonState()
    }

    override fun onResume() {
        super.onResume()
        chatViewModel.getGroupData(tokenManager.getAccessToken(),groupId)
        chatViewModel.getGroupMembers(tokenManager.getAccessToken(),groupId)

    }

    private fun updateButtonState (){
        val name = nameEditText.string()
        applyButton.isEnabled = oldAvatarUrl== null || name!=oldName
    }
}