package com.emil.linksy.presentation.ui.navigation.channel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.emil.linksy.adapters.AppreciatedAdapter

import com.emil.linksy.presentation.viewmodel.ChannelViewModel
import com.emil.linksy.util.TokenManager
import com.emil.presentation.R
import com.emil.presentation.databinding.AppreciatedDialogBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AppreciatedDialogFragment:DialogFragment() {
    private lateinit var binding:AppreciatedDialogBinding
    private val channelViewModel: ChannelViewModel by viewModel<ChannelViewModel>()
    private val tokenManager: TokenManager by inject()
    private  var postId: Long = -1

    companion object {
        private const val ARG_POST_ID = "POST_ID"

        fun newInstance(postId:Long): AppreciatedDialogFragment {
            val fragment = AppreciatedDialogFragment()
            val args = Bundle()
            args.putLong(ARG_POST_ID, postId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun getTheme() = R.style.FullScreenDialog

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId =  it.getLong(ARG_POST_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

      binding = AppreciatedDialogBinding.inflate(layoutInflater)
        val view = binding.root
        return  view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tb.setNavigationOnClickListener {
           dismiss()
        }
        binding.rvEvalutions.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = AppreciatedAdapter ()
        binding.rvEvalutions.adapter = adapter
        channelViewModel.getPostAppreciated(tokenManager.getAccessToken(),postId, onSuccess = {}, onError = {})
        channelViewModel.appreciatedList.observe(requireActivity()){list ->
            adapter.submitList(list)
        }
    }

}