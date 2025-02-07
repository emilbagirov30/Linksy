package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.emil.domain.model.MomentResponse
import com.emil.linksy.adapters.MomentsFullScreenAdapter
import com.emil.linksy.presentation.ui.navigation.feed.SubscriptionsPostsFeedFragment
import com.emil.linksy.presentation.viewmodel.MomentViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.presentation.R
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FullScreenMomentDialogFragment(private val userId:Long = -1,private val unseen:Boolean = false,
                                     private val momentsList: List<MomentResponse> = emptyList(),
                                     private val position:Int,
                                     private val subscriptionsPostsFeedFragment: SubscriptionsPostsFeedFragment? = null): DialogFragment()  {
    private lateinit var closeDialog: ImageButton
    private lateinit var fullscreenMomentsRecyclerView: RecyclerView
    private lateinit var adapter: MomentsFullScreenAdapter
    private val momentViewModel:MomentViewModel by viewModel<MomentViewModel>()
    private val tokenManager:TokenManager by inject<TokenManager> ()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return inflater.inflate(R.layout.dialog_fullscreen_moment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeDialog = view.findViewById(R.id.ib_close)
        fullscreenMomentsRecyclerView = view.findViewById(R.id.rv_fullscreen_moments)
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        fullscreenMomentsRecyclerView.layoutManager = layoutManager
        if(unseen ){
            momentViewModel.getUnseenUserMoments(tokenManager.getAccessToken(),userId)
            momentViewModel.momentList.observe(requireActivity()){momentsList ->
                adapter = MomentsFullScreenAdapter(momentsList, requireActivity(), this,momentViewModel, tokenManager )
                fullscreenMomentsRecyclerView.adapter = adapter
                fullscreenMomentsRecyclerView.scrollToPosition(position)
            }
        }else{
            adapter = MomentsFullScreenAdapter(momentsList, requireActivity(), this,momentViewModel, tokenManager )
            fullscreenMomentsRecyclerView.adapter = adapter
            fullscreenMomentsRecyclerView.scrollToPosition(position)
        }


        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(fullscreenMomentsRecyclerView)
        closeDialog.setOnClickListener {
            it.anim()
            dialog?.dismiss()
            adapter.releaseAllResources()

        }
    }

    override fun getTheme() = R.style.FullScreenDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }
    override fun onDismiss(dialog: android.content.DialogInterface) {
        super.onDismiss(dialog)
        adapter.releaseAllResources()
        subscriptionsPostsFeedFragment?.getData()
    }
}