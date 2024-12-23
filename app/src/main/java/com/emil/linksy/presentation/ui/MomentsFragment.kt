package com.emil.linksy.presentation.ui


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.emil.linksy.util.anim
import com.emil.presentation.R


class MomentsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_moment, container, false)
        val addMomentButton = view.findViewById<TextView>(R.id.tv_add)
        addMomentButton.setOnClickListener {
           it.anim()
            CreateMomentDialogFragment().show(parentFragmentManager, "CreateMomentDialogFragment")
        }
        return view
    }



}