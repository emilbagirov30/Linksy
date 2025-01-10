package com.emil.linksy.presentation.ui.navigation.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emil.linksy.adapters.ChatsAdapter
import com.emil.linksy.presentation.viewmodel.ChatViewModel
import com.emil.linksy.util.TokenManager
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatFragment : Fragment() {

    private lateinit var chatRecyclerView: RecyclerView
    private val chatViewModel: ChatViewModel by viewModel<ChatViewModel>()
    private val tokenManager: TokenManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        val toolbar = view.findViewById<MaterialToolbar>(R.id.tb)
        toolbar.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.chat_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                return when (menuItem.itemId) {
                    R.id.action_create_group -> {
                        startActivity(Intent(context,CreateGroupActivity::class.java))
                        true
                    }
                    else -> false
                }
            }
        }, requireActivity(), Lifecycle.State.CREATED)


        chatRecyclerView = view.findViewById(R.id.rv_chats)
        chatRecyclerView.layoutManager = LinearLayoutManager(context)

        chatViewModel.chatList.observe(requireActivity()) { chatlist ->
            chatRecyclerView.adapter =
                ChatsAdapter(chatlist, requireContext())
            chatlist.map { c ->
                chatViewModel.insertChat(c)
            }
        }
        chatViewModel.getUserChats(tokenManager.getAccessToken(), onError = {
            chatViewModel.getUserChatsFromLocalDb()
        })

        return view
    }



}


