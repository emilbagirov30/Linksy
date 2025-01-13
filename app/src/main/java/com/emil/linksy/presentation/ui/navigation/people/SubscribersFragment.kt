package com.emil.linksy.presentation.ui.navigation.people

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emil.linksy.adapters.UsersAdapter
import com.emil.linksy.presentation.viewmodel.PeopleViewModel
import com.emil.linksy.util.TokenManager
import com.emil.presentation.R
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class SubscribersFragment : Fragment() {

    private val peopleViewModel: PeopleViewModel by viewModel<PeopleViewModel>()
    private val tokenManager: TokenManager by inject()
    private lateinit var userRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_subscribers, container, false)

        userRecyclerView = view.findViewById(R.id.rv_users)
        userRecyclerView.layoutManager = LinearLayoutManager(context)

        peopleViewModel.userList.observe(requireActivity()){ userlist ->
            userRecyclerView.adapter =
                UsersAdapter(userList = userlist.toMutableList(),context = requireContext())
        }
        peopleViewModel.getUserSubscribers(tokenManager.getAccessToken())
        return view
    }


}