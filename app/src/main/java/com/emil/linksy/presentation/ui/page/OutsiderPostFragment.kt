package com.emil.linksy.presentation.ui.page

import android.annotation.SuppressLint
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
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.google.android.material.textview.MaterialTextView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class OutsiderPostFragment: Fragment() {
    private var userId:Long = -1

    companion object{
        private const val USER_ID = "USER_ID"
        fun newInstance(userId: Long):  OutsiderPostFragment {
            val fragment = OutsiderPostFragment()
            val args = Bundle()
            args.putLong(USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
    }


    private val postViewModel: PostViewModel by viewModel<PostViewModel>()
    private lateinit var postsRecyclerView:RecyclerView
    private lateinit var loading:ProgressBar
    private val tokenManager:TokenManager by inject<TokenManager>()
    private lateinit var emptyTextView: MaterialTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getLong(USER_ID)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return inflater.inflate(R.layout.fragment_outsider_post, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postsRecyclerView = view.findViewById(R.id.rv_posts)
        emptyTextView = view.findViewById(R.id.tv_empty_content)
        loading = view.findViewById(R.id.pb_loading)
        postsRecyclerView.layoutManager = LinearLayoutManager(context)
        updatePosts()
    }

    private fun updatePosts (){
        postViewModel.getOutsiderUserPosts(tokenManager.getAccessToken(),userId, onSuccess = {
            loading.hide()

        })
        postViewModel.postList.observe(requireActivity()){ postlist ->
            if (postlist.isEmpty()) emptyTextView.show()
            else{
                postsRecyclerView.show()
                postsRecyclerView.adapter = PostsAdapter(postlist,postViewModel, context = requireContext(),tokenManager, outsiderPostFragment = this)
            }


        }
    }
}