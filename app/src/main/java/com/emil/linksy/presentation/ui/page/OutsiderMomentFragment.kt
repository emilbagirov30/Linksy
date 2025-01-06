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
import com.emil.linksy.presentation.viewmodel.MomentViewModel
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.google.android.flexbox.FlexboxLayout
import org.koin.androidx.viewmodel.ext.android.viewModel


class OutsiderMomentFragment(private val id:Long) : Fragment() {
    private val momentViewModel: MomentViewModel by viewModel<MomentViewModel>()
    private  lateinit var contentFlexboxLayout: FlexboxLayout
    private lateinit var loading: ProgressBar
    private lateinit var momentsRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_outsider_moment, container, false)
        loading = view.findViewById(R.id.pb_loading)
        momentsRecyclerView = view.findViewById(R.id.rv_moments)
        val layoutManager = GridLayoutManager(context, 4)
        momentsRecyclerView.layoutManager = layoutManager
        contentFlexboxLayout = view.findViewById(R.id.fl_moments)
        updateMoments()





        return view
    }
    private fun updateMoments (){
        momentViewModel.getOutsiderUserMoments(id, onSuccess = {loading.hide()
        contentFlexboxLayout.show()
        })
        momentViewModel.momentList.observe(requireActivity()){ momentlist ->
            momentsRecyclerView.adapter =
                MomentsAdapter(momentlist,momentViewModel, context = requireContext())

        }
    }
}