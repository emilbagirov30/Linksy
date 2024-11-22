package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emil.linksy.adapters.PostsAdapter
import com.emil.linksy.presentation.viewmodel.PostViewModel
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.presentation.R
import org.koin.androidx.viewmodel.ext.android.viewModel


class PostFragment : Fragment() {

    private lateinit var newPostEditText:EditText
    private lateinit var postsRecyclerView:RecyclerView
    private lateinit var sharedPref: SharedPreferences
    private  lateinit var emptyMessage:FrameLayout

private val postViewModel: PostViewModel by viewModel<PostViewModel>()
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
        postsRecyclerView = view.findViewById(R.id.rv_posts)
        emptyMessage = view.findViewById(R.id.fl_empty_message)
        sharedPref = requireContext().getSharedPreferences("TokenData", Context.MODE_PRIVATE)
        postsRecyclerView.layoutManager = LinearLayoutManager(context)
        val token = sharedPref.getString("ACCESS_TOKEN",null).toString()
        postViewModel.getUserPosts(token, onError = {})
        postViewModel.postList.observe(requireActivity()){ postlist ->
            postsRecyclerView.adapter = PostsAdapter(postlist,postViewModel,context = requireContext())
            if (postlist.isNotEmpty()) {
                showContent()
            }
        }
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

    private fun showContent (){
        postsRecyclerView.show()
        emptyMessage.hide()
    }

}