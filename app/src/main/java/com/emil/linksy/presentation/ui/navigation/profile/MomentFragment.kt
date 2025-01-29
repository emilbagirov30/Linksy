package com.emil.linksy.presentation.ui.navigation.profile


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emil.linksy.adapters.MomentsAdapter
import com.emil.linksy.presentation.viewmodel.MomentViewModel
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.flexbox.FlexboxLayout
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MomentFragment : Fragment(),CreateMomentDialogFragment.AddMomentDialogListener {
    private lateinit var momentsRecyclerView:RecyclerView
    private  lateinit var emptyMessage: LinearLayout
    private  lateinit var contentFlexboxLayout:FlexboxLayout
    private  lateinit var shimmerMoments: ShimmerFrameLayout
    private val tokenManager: TokenManager by inject()
    private val momentViewModel: MomentViewModel by viewModel<MomentViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return inflater.inflate(R.layout.fragment_moment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addMoment = view.findViewById<FrameLayout>(R.id.fl_add)
        emptyMessage = view.findViewById(R.id.ll_empty_message)
        contentFlexboxLayout = view.findViewById(R.id.fl_moments)
        momentsRecyclerView = view.findViewById(R.id.rv_moments)
        shimmerMoments= view.findViewById(R.id.shimmer_moments)
        shimmerMoments.setShimmer(Linksy.CUSTOM_SHIMMER)
        shimmerMoments.startShimmer()
        val layoutManager = GridLayoutManager(context, 4)
        momentsRecyclerView.layoutManager = layoutManager

        updateMoments()
        addMoment.setOnClickListener {
            val dialog =   CreateMomentDialogFragment()
            dialog.setAddMomentDialogListener(this)
            dialog.show(parentFragmentManager, "CreateMomentDialogFragment")
        }
    }


    private fun showEmptyMessage (){
       contentFlexboxLayout.hide()
        emptyMessage.show()
    }
    private fun stopShimmer(){
        shimmerMoments.stopShimmer()
    }

    private fun showContent(){
        shimmerMoments.hide()
        emptyMessage.hide()
        contentFlexboxLayout.show()
    }
      private fun updateMoments (){
        val token = tokenManager.getAccessToken()
        momentViewModel.getUserMoments(token, onSuccess = {stopShimmer()}, onError = {stopShimmer()})
        momentViewModel.momentList.observe(requireActivity()){ momentlist ->
           momentsRecyclerView.adapter =
               context?.let { MomentsAdapter(momentlist,momentViewModel, context = it, tokenManager = tokenManager) }
                if (momentlist.isEmpty()) showEmptyMessage()
                else showContent()
        }
    }

    override fun onMomentAdded() {
        updateMoments()
    }


}