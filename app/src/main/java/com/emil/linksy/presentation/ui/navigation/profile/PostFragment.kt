package com.emil.linksy.presentation.ui.navigation.profile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emil.linksy.adapters.PostsAdapter
import com.emil.linksy.presentation.viewmodel.PostViewModel
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.facebook.shimmer.ShimmerFrameLayout
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class PostFragment : Fragment(), AddPostDialogFragment.AddPostDialogListener {

    private lateinit var newPostEditText:EditText
    private lateinit var postsRecyclerView:RecyclerView
    private  lateinit var emptyMessage:LinearLayout
    private  lateinit var shimmerPosts:ShimmerFrameLayout
    private val postViewModel: PostViewModel by viewModel<PostViewModel>()
    private val tokenManager: TokenManager by inject()


    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      return inflater.inflate(R.layout.fragment_post, container, false)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newPostEditText = view.findViewById(R.id.et_new_post)
        postsRecyclerView = view.findViewById(R.id.rv_posts)
        emptyMessage = view.findViewById(R.id.ll_empty_message)
        shimmerPosts = view.findViewById(R.id.shimmer_posts)
        shimmerPosts.setShimmer(Linksy.CUSTOM_SHIMMER)
        shimmerPosts.startShimmer()
        postsRecyclerView.layoutManager = LinearLayoutManager(context)

        newPostEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val dialog = AddPostDialogFragment()
                dialog.setAddPostDialogListener(this)
                dialog.show(parentFragmentManager, "AddPostDialogFragment")
                true
            } else {
                false
            }
        }
    }


    private fun showContent (){
        shimmerPosts.hide()
        postsRecyclerView.show()
        emptyMessage.hide()
    }
    private fun stopShimmer(){
        shimmerPosts.stopShimmer()

    }
    private fun showEmptyMessage (){
        shimmerPosts.hide()
        postsRecyclerView.hide()
        emptyMessage.show()
    }

    override fun onResume() {
        super.onResume()
        if (postViewModel.postList.value.isNullOrEmpty()) updatePosts()
    }

       private fun updatePosts (){
       if (!isAdded || view == null) return
       val token = tokenManager.getAccessToken()
       postViewModel.getUserPosts(token, onSuccess = {stopShimmer()}, onError = {stopShimmer()})
       postViewModel.postList.observe(viewLifecycleOwner){ postlist ->
           postsRecyclerView.adapter = context?.let {
               PostsAdapter(postlist,postViewModel,
                   context = it,
                   tokenManager = tokenManager, postFragment = this)
           }
           if (postlist.isEmpty()) showEmptyMessage()
           else showContent()
       }
   }

    override fun onPostAdded() {
        updatePosts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        postViewModel.postList.removeObservers(viewLifecycleOwner)
    }
}