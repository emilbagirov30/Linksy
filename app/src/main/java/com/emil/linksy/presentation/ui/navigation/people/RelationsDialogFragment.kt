package com.emil.linksy.presentation.ui.navigation.people

import android.annotation.SuppressLint
import android.os.Build
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

class RelationsDialogFragment : DialogFragment() {

    private lateinit var userRecyclerView: RecyclerView
    private val peopleViewModel: PeopleViewModel by viewModel()
    private val channelViewModel: ChannelViewModel by viewModel()
    private val chatViewModel: ChatViewModel by viewModel()
    private val postViewModel: PostViewModel by viewModel()
    private val tokenManager: TokenManager by inject()

    private var type: RelationType = RelationType.SUBSCRIBERS
    private var userId: Long = -1
    private var username: String = ""
    private var channelId: Long = -1
    private var postId: Long = -1
    private var groupId: Long = -1
    private var memberIdList: List<Long> = emptyList()

    override fun getTheme() = R.style.FullScreenDialog

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_TYPE,RelationType::class.java)!!
            }else it.getParcelable(ARG_TYPE)!! //Deprecation
            userId = it.getLong(ARG_USER_ID)
            username = it.getString(ARG_USERNAME, "")
            channelId = it.getLong(ARG_CHANNEL_ID)
            postId = it.getLong(ARG_POST_ID)
            groupId = it.getLong(ARG_GROUP_ID)
            memberIdList = it.getLongArray(ARG_MEMBER_ID_LIST)?.toList() ?: emptyList()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }

    @SuppressLint("MissingInflatedId", "SetTextI18n", "SuspiciousIndentation")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.relations_dialog, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolBar = view.findViewById<MaterialToolbar>(R.id.tb)
        val titleTextView = view.findViewById<MaterialTextView>(R.id.tv_title)
        userRecyclerView = view.findViewById(R.id.rv_users)
        val inviteButton = view.findViewById<MaterialButton>(R.id.bt_invite)
        userRecyclerView.layoutManager = LinearLayoutManager(context)

        when (type) {
            RelationType.SUBSCRIBERS -> {
                titleTextView.text = "$username\n${getString(R.string.subscribers)}"
                peopleViewModel.getOutsiderUserSubscribers(userId)
            }
            RelationType.SUBSCRIPTIONS -> {
                titleTextView.text = "$username\n${getString(R.string.subscriptions)}"
                peopleViewModel.getOutsiderUserSubscriptions(userId)
            }
            RelationType.REQUESTS -> {
                titleTextView.text = getString(R.string.subscription_request)
                channelViewModel.getChannelSubscriptionRequest(
                    tokenManager.getAccessToken(),
                    channelId = channelId,
                    onConflict = {}
                )
            }
            RelationType.CHANNEL_MEMBERS -> {
                titleTextView.text = getString(R.string.subscribers)
                channelViewModel.getChannelMembers(
                    tokenManager.getAccessToken(),
                    channelId,
                    onConflict = {}
                )
            }
            RelationType.LIKES -> {
                titleTextView.text = getString(R.string.likes)
                postViewModel.getLikes(
                    tokenManager.getAccessToken(),
                    postId = postId,
                    onSuccess = {},
                    onError = {}
                )
            }
            RelationType.ADD_MEMBERS -> {
                inviteButton.show()
                titleTextView.text = getString(R.string.addMembers)
                peopleViewModel.getUserSubscribers(tokenManager.getAccessToken())
            }
        }

        postViewModel.likesList.observe(requireActivity()) { list ->
            userRecyclerView.adapter =
                UsersAdapter(userList = list.toMutableList(), context = requireContext())
        }

        channelViewModel.memberList.observe(this) { memberList ->
            userRecyclerView.adapter =
                UsersAdapter(userList = memberList.toMutableList(), context = requireContext())
        }

        channelViewModel.subscriptionsRequestList.observe(this) { requestList ->
            userRecyclerView.adapter =
                UsersAdapter(userList = requestList.toMutableList(), context = requireContext(), channelId = channelId,
                    isChannelAdmin = true, channelViewModel = channelViewModel, tokenManager = tokenManager)
        }

        peopleViewModel.userList.observe(requireActivity()) { userlist ->
            val filterList = userlist.filter { !memberIdList.contains(it.id) }
            if (type == RelationType.ADD_MEMBERS) {
                val adapter = UsersAdapter(userList = filterList.toMutableList(), context = requireContext(), isNeedChoice = true)
                userRecyclerView.adapter = adapter
                inviteButton.setOnClickListener {
                    if (adapter.getSelectedUserIds().isNotEmpty())
                        chatViewModel.addMembers(tokenManager.getAccessToken(), groupId, adapter.getSelectedUserIds(), onSuccess = { dismiss() })
                }
            } else {
                userRecyclerView.adapter =
                    UsersAdapter(userList = userlist.toMutableList(), context = requireContext())
            }
        }

        toolBar.setNavigationOnClickListener {
            dismiss()
        }
    }

    companion object {
        private const val ARG_TYPE = "arg_type"
        private const val ARG_USER_ID = "arg_user_id"
        private const val ARG_USERNAME = "arg_username"
        private const val ARG_CHANNEL_ID = "arg_channel_id"
        private const val ARG_POST_ID = "arg_post_id"
        private const val ARG_GROUP_ID = "arg_group_id"
        private const val ARG_MEMBER_ID_LIST = "arg_member_id_list"

        fun newInstance(
            type: RelationType,
            userId: Long = -1,
            username: String = "",
            channelId: Long = -1,
            postId: Long = -1,
            groupId: Long = -1,
            memberIdList: List<Long> = emptyList()
        ): RelationsDialogFragment {
            val fragment = RelationsDialogFragment()
            val args = Bundle().apply {
                putParcelable(ARG_TYPE, type)
                putLong(ARG_USER_ID, userId)
                putString(ARG_USERNAME, username)
                putLong(ARG_CHANNEL_ID, channelId)
                putLong(ARG_POST_ID, postId)
                putLong(ARG_GROUP_ID, groupId)
                putLongArray(ARG_MEMBER_ID_LIST, memberIdList.toLongArray())
            }
            fragment.arguments = args
            return fragment
        }
    }
}
