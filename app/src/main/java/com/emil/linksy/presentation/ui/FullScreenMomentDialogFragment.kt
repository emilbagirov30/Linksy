package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.emil.domain.model.MomentResponse
import com.emil.linksy.adapters.MomentsAdapter
import com.emil.linksy.adapters.MomentsFullScreenAdapter
import com.emil.linksy.util.anim
import com.emil.presentation.R

class FullScreenMomentDialogFragment(private val momentsList: List<MomentResponse>,private val position:Int): DialogFragment()  {
    private lateinit var closeDialog: ImageButton
    private lateinit var fullscreenMomentsRecyclerView: RecyclerView
    private lateinit var adapter: MomentsFullScreenAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fullscreen_moment, container, false)
        closeDialog = view.findViewById(R.id.ib_close)
        fullscreenMomentsRecyclerView = view.findViewById(R.id.rv_fullscreen_moments)

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        fullscreenMomentsRecyclerView.layoutManager = layoutManager
        adapter = MomentsFullScreenAdapter(momentsList, requireContext(), this)
        fullscreenMomentsRecyclerView.adapter = adapter
        fullscreenMomentsRecyclerView.scrollToPosition(position)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(fullscreenMomentsRecyclerView)
        closeDialog.setOnClickListener {
            it.anim()
            dialog?.dismiss()
            adapter.releaseAllResources()

        }
        return view
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
    override fun onDismiss(dialog: android.content.DialogInterface) {
        super.onDismiss(dialog)
        adapter.releaseAllResources()
    }
}