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
import com.emil.linksy.presentation.viewmodel.PeopleViewModel
import com.emil.linksy.util.RelationType
import com.emil.linksy.util.TokenManager
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RelationsDialogFragment(val type: RelationType, val userId:Long=-1, val username:String="",val channelId:Long = -1): DialogFragment() {
    private lateinit var userRecyclerView: RecyclerView
    private val peopleViewModel: PeopleViewModel by viewModel<PeopleViewModel>()
    private val channelViewModel: ChannelViewModel by viewModel<ChannelViewModel>()
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
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.outsider_relations_dialog, container, false)
        val toolBar = view.findViewById<MaterialToolbar>(R.id.tb)
        val titleTextView = view.findViewById<MaterialTextView>(R.id.tv_title)
        userRecyclerView = view.findViewById(R.id.rv_users)
        userRecyclerView.layoutManager = LinearLayoutManager(context)
        if (type == RelationType.SUBSCRIBERS){
            titleTextView.text = "$username\n${getString(R.string.subscribers)}"
            peopleViewModel.getOutsiderUserSubscribers(userId)
        }else if  (type == RelationType.SUBSCRIPTIONS){
            titleTextView.text = "$username\n${getString(R.string.subscriptions)}"
            peopleViewModel.getOutsiderUserSubscriptions(userId)
        } else if (type == RelationType.REQUESTS){
            titleTextView.text = "$username\n${getString(R.string.subscription_request)}"
            channelViewModel.getChannelSubscriptionRequest(tokenManager.getAccessToken(), channelId =channelId , onConflict = {})
        }else if (type == RelationType.CHANNEL_MEMBERS){
              channelViewModel.getChannelMembers(tokenManager.getAccessToken(),channelId, onConflict = {})
        }


        channelViewModel.memberList.observe(this){memberList ->
            userRecyclerView.adapter =
                UsersAdapter(userList = memberList.toMutableList(),context = requireContext())
        }
        channelViewModel.subscriptionsRequestList.observe(this){requestList ->
            userRecyclerView.adapter =
                UsersAdapter(userList = requestList.toMutableList(),context = requireContext(), channelId = channelId, isChannelAdmin = true, channelViewModel = channelViewModel, tokenManager = tokenManager)
        }






        peopleViewModel.userList.observe(requireActivity()){ userlist ->
            userRecyclerView.adapter =
                UsersAdapter(userList = userlist.toMutableList(),context = requireContext())
        }
        toolBar.setNavigationOnClickListener {
           dismiss()
        }

        return view
    }





}