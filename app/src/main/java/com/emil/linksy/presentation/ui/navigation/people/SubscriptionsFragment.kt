package com.emil.linksy.presentation.ui.navigation.people

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.emil.linksy.adapters.UsersAdapter
import com.emil.linksy.presentation.viewmodel.PeopleViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.presentation.R
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class SubscriptionsFragment : Fragment() {
    private val peopleViewModel: PeopleViewModel by viewModel<PeopleViewModel>()
    private val tokenManager: TokenManager by inject()
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return inflater.inflate(R.layout.fragment_subscriptions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        userRecyclerView = view.findViewById(R.id.rv_users)
        userRecyclerView.layoutManager = LinearLayoutManager(context)
        val loading = view.findViewById<ProgressBar>(R.id.pb_loading)
        loading.show()
        peopleViewModel.userList.observe(requireActivity()){ userlist ->
            userRecyclerView.adapter =
                context?.let { UsersAdapter(userList = userlist.toMutableList(),context = it) }
        }
        peopleViewModel.getUserSubscriptions(tokenManager.getAccessToken(), onSuccess = {
            loading.hide()
            userRecyclerView.show()
        })
        swipeRefreshLayout.setOnRefreshListener {
            getUserSubscriptions()
        }
    }

    private fun getUserSubscriptions(){
        peopleViewModel.getUserSubscriptions(tokenManager.getAccessToken(), onSuccess = {
            swipeRefreshLayout.isRefreshing = false
        })
    }


}