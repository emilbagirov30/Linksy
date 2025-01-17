package com.emil.linksy.presentation.ui.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emil.linksy.adapters.MomentsAdapter
import com.emil.linksy.presentation.ui.navigation.channel.AddChannelPostDialogFragment
import com.emil.linksy.presentation.viewmodel.MomentViewModel
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.textview.MaterialTextView
import org.koin.androidx.viewmodel.ext.android.viewModel


class OutsiderMomentFragment() : Fragment() {

     private var userId:Long = -1

    companion object{
        private const val USER_ID = "USER_ID"
        fun newInstance(userId: Long): OutsiderMomentFragment {
            val fragment = OutsiderMomentFragment()
            val args = Bundle()
            args.putLong(USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
    }


    private val momentViewModel: MomentViewModel by viewModel<MomentViewModel>()
    private  lateinit var contentFlexboxLayout: FlexboxLayout
    private lateinit var loading: ProgressBar
    private lateinit var momentsRecyclerView: RecyclerView
    private lateinit var emptyTextView: MaterialTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getLong(USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_outsider_moment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading = view.findViewById(R.id.pb_loading)
        emptyTextView = view.findViewById(R.id.tv_empty_content)
        momentsRecyclerView = view.findViewById(R.id.rv_moments)
        val layoutManager = GridLayoutManager(context, 4)
        momentsRecyclerView.layoutManager = layoutManager
        contentFlexboxLayout = view.findViewById(R.id.fl_moments)
        updateMoments()
    }


    private fun updateMoments (){
        momentViewModel.getOutsiderUserMoments(userId, onSuccess = {loading.hide()

        })
        momentViewModel.momentList.observe(requireActivity()){ momentlist ->
            if (momentlist.isEmpty()) emptyTextView.show()
            else {
                contentFlexboxLayout.show()
                momentsRecyclerView.adapter =
                    MomentsAdapter(momentlist, momentViewModel, context = requireContext())
            }
        }
    }
}