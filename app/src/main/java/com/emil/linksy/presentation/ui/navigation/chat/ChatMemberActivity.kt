package com.emil.linksy.presentation.ui.navigation.chat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emil.linksy.adapters.UsersAdapter
import com.emil.linksy.presentation.viewmodel.ChatViewModel
import com.emil.linksy.util.TokenManager
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatMemberActivity : AppCompatActivity() {
    private val chatViewModel: ChatViewModel by viewModel<ChatViewModel>()
    private val tokenManager: TokenManager by inject()
    private lateinit var userRecyclerView: RecyclerView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_member)
        val toolBar = findViewById<MaterialToolbar>(R.id.tb)
        userRecyclerView = findViewById(R.id.rv_users)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        val groupId = intent.getLongExtra("GROUP_ID", -1)


          chatViewModel.memberList.observe(this){ memberList ->
            userRecyclerView.adapter =
                UsersAdapter(userList = memberList,context = this)
        }
        chatViewModel.getGroupMembers(tokenManager.getAccessToken(),groupId)

        toolBar.setNavigationOnClickListener {
            finish()
        }

    }
}