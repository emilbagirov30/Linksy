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


class SubscribersFragment : Fragment() {

    private val peopleViewModel: PeopleViewModel by viewModel<PeopleViewModel>()
    private val tokenManager: TokenManager by inject()
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_subscribers, container, false)
        val loading = view.findViewById<ProgressBar>(R.id.pb_loading)
        loading.show()
        userRecyclerView = view.findViewById(R.id.rv_users)
        userRecyclerView.layoutManager = LinearLayoutManager(context)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        peopleViewModel.userList.observe(requireActivity()){ userlist ->
            userRecyclerView.adapter =
                context?.let { UsersAdapter(userList = userlist.toMutableList(),context = it)
                }
            swipeRefreshLayout.isRefreshing = false
        }
        peopleViewModel.getUserSubscribers(tokenManager.getAccessToken(), onSuccess = {
            loading.hide()
            userRecyclerView.show()

        })

        swipeRefreshLayout.setOnRefreshListener {
            getUserSubscribers()
        }
        return view
    }
       private fun getUserSubscribers(){
           peopleViewModel.getUserSubscribers(tokenManager.getAccessToken(), onSuccess = {})
       }

}