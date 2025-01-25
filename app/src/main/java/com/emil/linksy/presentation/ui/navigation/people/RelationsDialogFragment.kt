package com.emil.linksy.presentation.ui.navigation.people

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emil.linksy.adapters.UsersAdapter
import com.emil.linksy.presentation.viewmodel.ChannelViewModel
import com.emil.linksy.presentation.viewmodel.ChatViewModel
import com.emil.linksy.presentation.viewmodel.PeopleViewModel
import com.emil.linksy.presentation.viewmodel.PostViewModel
import com.emil.linksy.util.RelationType
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RelationsDialogFragment(val type: RelationType, val userId:Long=-1, val username:String="",
                              val channelId:Long = -1,val postId:Long = -1,val groupId:Long = -1,val memberIdList:List<Long> = emptyList()
): DialogFragment() {
    private lateinit var userRecyclerView: RecyclerView
    private val peopleViewModel: PeopleViewModel by viewModel<PeopleViewModel>()
    private val channelViewModel: ChannelViewModel by viewModel<ChannelViewModel>()
    private val chatViewModel: ChatViewModel by viewModel<ChatViewModel>()
    private val postViewModel:PostViewModel by viewModel<PostViewModel> ()
    private val tokenManager: TokenManager by inject()
    override fun getTheme() = R.style.FullScreenDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)

    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }
    @SuppressLint("MissingInflatedId", "SetTextI18n", "SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.relations_dialog, container, false)
        val toolBar = view.findViewById<MaterialToolbar>(R.id.tb)
        val titleTextView = view.findViewById<MaterialTextView>(R.id.tv_title)
        userRecyclerView = view.findViewById(R.id.rv_users)
        val inviteButton = view.findViewById<MaterialButton>(R.id.bt_invite)
        userRecyclerView.layoutManager = LinearLayoutManager(context)

        if (type == RelationType.SUBSCRIBERS) {
            titleTextView.text = "$username\n${getString(R.string.subscribers)}"
            peopleViewModel.getOutsiderUserSubscribers(userId)
        } else if (type == RelationType.SUBSCRIPTIONS) {
            titleTextView.text = "$username\n${getString(R.string.subscriptions)}"
            peopleViewModel.getOutsiderUserSubscriptions(userId)
        } else if (type == RelationType.REQUESTS) {
            titleTextView.text = getString(R.string.subscription_request)
            channelViewModel.getChannelSubscriptionRequest(
                tokenManager.getAccessToken(),
                channelId = channelId,
                onConflict = {})
        } else if (type == RelationType.CHANNEL_MEMBERS) {
            titleTextView.text = getString(R.string.subscribers)
            channelViewModel.getChannelMembers(
                tokenManager.getAccessToken(),
                channelId,
                onConflict = {})
        } else if (type == RelationType.LIKES) {
            titleTextView.text = getString(R.string.likes)
            postViewModel.getLikes(
                tokenManager.getAccessToken(),
                postId = postId,
                onSuccess = {},
                onError = {})
        } else if (type == RelationType.ADD_MEMBERS) {
            inviteButton.show()
            titleTextView.text = getString(R.string.addMembers)
            peopleViewModel.getUserSubscribers(tokenManager.getAccessToken())

        }


        postViewModel.likesList.observe(requireActivity()){list->
            userRecyclerView.adapter =
                UsersAdapter(userList = list.toMutableList(),context = requireContext())
        }


        channelViewModel.memberList.observe(this){memberList ->
            userRecyclerView.adapter =
                UsersAdapter(userList = memberList.toMutableList(),context = requireContext())
        }

        channelViewModel.subscriptionsRequestList.observe(this){requestList ->
            userRecyclerView.adapter =
                UsersAdapter(userList = requestList.toMutableList(),context = requireContext(), channelId = channelId,
                    isChannelAdmin = true, channelViewModel = channelViewModel, tokenManager = tokenManager)
        }

        peopleViewModel.userList.observe(requireActivity()){ userlist ->
            val filterList = userlist.filter { !memberIdList.contains(it.id) }
            if (type ==RelationType.ADD_MEMBERS ){
                val adapter =  UsersAdapter(userList = filterList.toMutableList(), context = requireContext(),isNeedChoice = true)
                userRecyclerView.adapter = adapter
                inviteButton.setOnClickListener {
                    if (adapter.getSelectedUserIds().isNotEmpty())
                    chatViewModel.addMembers(tokenManager.getAccessToken(),groupId, adapter.getSelectedUserIds(), onSuccess = {dismiss()})
                }

            }else {
                userRecyclerView.adapter =
                    UsersAdapter(userList = userlist.toMutableList(), context = requireContext())
            }
        }


        toolBar.setNavigationOnClickListener {
           dismiss()
        }

        return view
    }





}