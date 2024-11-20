package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import com.emil.presentation.R


class PostFragment : Fragment() {

private lateinit var newPostEditText:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_post, container, false)
        newPostEditText = view.findViewById(R.id.et_new_post)
        newPostEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                AddPostDialogFragment().show(parentFragmentManager, "AddPostDialogFragment")
                true
            } else {
                false
            }
        }

        return view
    }


}