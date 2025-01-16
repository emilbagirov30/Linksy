package com.emil.linksy.presentation.ui.navigation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.emil.linksy.adapters.UsersAdapter
import com.emil.linksy.presentation.viewmodel.PeopleViewModel
import com.emil.linksy.presentation.viewmodel.UserViewModel
import com.emil.linksy.util.TokenManager
import com.emil.presentation.R
import com.emil.presentation.databinding.BlacklistDialogBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class BlackListDialogFragment:DialogFragment () {

private lateinit var binding:BlacklistDialogBinding
    private val userViewModel: UserViewModel by viewModel<UserViewModel>()
    private val peopleViewModel: PeopleViewModel by viewModel<PeopleViewModel>()
    private val tokenManager: TokenManager by inject()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BlacklistDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.getBlackList(tokenManager.getAccessToken())
        userViewModel.blacklist.observe(requireActivity()){ bl ->
            binding.rvBlacklist.layoutManager = LinearLayoutManager(requireContext())
            binding.rvBlacklist.adapter = UsersAdapter(bl.toMutableList(),requireContext(), isBlackList = true,
                peopleViewModel = peopleViewModel, tokenManager = tokenManager)
        }


        binding.tb.setNavigationOnClickListener {
            dismiss()
        }
    }












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

}