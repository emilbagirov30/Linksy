package com.emil.linksy.presentation.ui.navigation.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emil.linksy.adapters.ChatsAdapter
import com.emil.linksy.presentation.viewmodel.ChatViewModel
import com.emil.linksy.util.TokenManager
import com.emil.presentation.R
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatFragment : Fragment() {

    private lateinit var chatRecyclerView: RecyclerView
    private val chatViewModel:ChatViewModel by viewModel<ChatViewModel>()
    private val tokenManager: TokenManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_chat, container, false)
        chatRecyclerView = view.findViewById(R.id.rv_chats)
        chatRecyclerView.layoutManager = LinearLayoutManager(context)

        chatViewModel.chatList.observe(requireActivity()){ chatlist ->
            chatRecyclerView.adapter =
                ChatsAdapter(chatlist,requireContext())
            chatlist.map { c->
                chatViewModel.insertChat(c)
            }
        }
        chatViewModel.getUserChats(tokenManager.getAccessToken(), onError = {
            chatViewModel.getUserChatsFromLocalDb ()
        })
        return view
    }

}