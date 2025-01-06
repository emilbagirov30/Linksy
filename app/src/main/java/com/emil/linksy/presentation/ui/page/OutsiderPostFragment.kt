package com.emil.linksy.presentation.ui.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emil.linksy.adapters.PostsAdapter
import com.emil.linksy.presentation.viewmodel.PostViewModel
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.presentation.R
import org.koin.androidx.viewmodel.ext.android.viewModel


class OutsiderPostFragment (private val id:Long): Fragment() {
    private val postViewModel: PostViewModel by viewModel<PostViewModel>()
    private lateinit var postsRecyclerView:RecyclerView
    private lateinit var loading:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_outsider_post, container, false)
        postsRecyclerView = view.findViewById(R.id.rv_posts)
        loading = view.findViewById(R.id.pb_loading)
        postsRecyclerView.layoutManager = LinearLayoutManager(context)
        updatePosts()



        return view
    }
    private fun updatePosts (){
        postViewModel.getOutsiderUserPosts(id, onSuccess = {
            loading.hide()
            postsRecyclerView.show()
        })
        postViewModel.postList.observe(requireActivity()){ postlist ->
            postsRecyclerView.adapter = PostsAdapter(postlist,postViewModel, context = requireContext())

        }
    }
}