package com.emil.linksy.presentation.ui.navigation.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.emil.domain.model.CommentData
import com.emil.linksy.adapters.CommentsAdapter
import com.emil.linksy.presentation.viewmodel.ChannelViewModel
import com.emil.linksy.presentation.viewmodel.PostViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.string
import com.emil.presentation.R
import com.emil.presentation.databinding.BottomSheetCommentsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommentsBottomSheet : BottomSheetDialogFragment() {
    private var postId: Long = -100L
    private var channelPostId: Long = -100L
    private var userId: Long = -1L
    private var parentCommentId:Long? = null
    private lateinit var binding: BottomSheetCommentsBinding
    private val postViewModel:PostViewModel by viewModel<PostViewModel>()
    private val channelViewModel:ChannelViewModel  by viewModel<ChannelViewModel>()
    private val tokenManager:TokenManager by inject<TokenManager> ()
    companion object {
        private const val ARG_CHANNEL_POST_ID = "CHANNEL_POST_ID"
        private const val ARG_POST_ID = "POST_ID"
        private const val ARG_USER_ID = "USER_ID"
        fun newInstance(channelPostId:Long = -100,postId: Long = -100,userId:Long): CommentsBottomSheet {
            val fragment = CommentsBottomSheet()
            val args = Bundle()
            args.putLong(ARG_CHANNEL_POST_ID,channelPostId)
            args.putLong(ARG_POST_ID, postId)
            args.putLong(ARG_USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun getTheme() = R.style.BottomSheetDialogTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        channelPostId = arguments?.getLong(ARG_CHANNEL_POST_ID)!!
        postId = arguments?.getLong(ARG_POST_ID)!!
        userId = arguments?.getLong(ARG_USER_ID)!!
        isCancelable = false

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomSheetCommentsBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog?.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val layoutParams = it.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                it.layoutParams = layoutParams
            }
        }
        binding.ibClose.setOnClickListener {
            it.anim()
            dismiss()
        }
        if (postId!=-100L) {
          getUserPostComments()
            binding.swipeRefreshLayout.setOnRefreshListener {
                getUserPostComments()
            }
            postViewModel.commentList.observe(requireActivity()) { commentlist ->
                binding.tvTitle.text = "${getString(R.string.comments)}: ${commentlist.size}"
                binding.rvComments.layoutManager = LinearLayoutManager(requireContext())

                val independentCommentList = commentlist.filter { c -> c.parentCommentId == null }
                binding.rvComments.adapter = CommentsAdapter(
                    userId,
                    independentCommentList,
                    allCommentList = commentlist,
                    context = requireActivity(),
                    commentsBottomSheet = this,
                    postViewModel = postViewModel,
                    tokenManager = tokenManager
                )
            }
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    p0: CharSequence?, p1: Int, p2: Int, p3: Int
                ) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().trim().isNotEmpty())
                        binding.ibSend.show()
                    else binding.ibSend.hide()
                }

                override fun afterTextChanged(p0: Editable?) {}
            }
            binding.etComment.addTextChangedListener(textWatcher)

            binding.ibSend.setOnClickListener {
                val text = binding.etComment.string()
                postViewModel.addComment(tokenManager.getAccessToken(), CommentData(postId, text, parentCommentId),
                    onSuccess = {
                        binding.etComment.setText("")
                        binding.llReplyInfo.hide()
                        parentCommentId = null
                        getUserPostComments()
                    },
                    onError = {})
            }
        }
        if (channelPostId!=-100L) {
             getChannelsPostComments()

            binding.swipeRefreshLayout.setOnRefreshListener {
                getChannelsPostComments()
            }
            channelViewModel.commentList.observe(requireActivity()) { commentlist ->
                binding.tvTitle.text = "${getString(R.string.comments)}: ${commentlist.size}"
                binding.rvComments.layoutManager = LinearLayoutManager(requireContext())

                val independentCommentList = commentlist.filter { c -> c.parentCommentId == null }
                binding.rvComments.adapter = CommentsAdapter(
                    userId,
                    independentCommentList,
                    allCommentList = commentlist,
                    context = requireActivity(),
                    commentsBottomSheet = this,
                    channelViewModel = channelViewModel,
                    tokenManager = tokenManager
                )
            }
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    p0: CharSequence?, p1: Int, p2: Int, p3: Int
                ) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().trim().isNotEmpty())
                        binding.ibSend.show()
                    else binding.ibSend.hide()
                }

                override fun afterTextChanged(p0: Editable?) {}
            }
            binding.etComment.addTextChangedListener(textWatcher)

            binding.ibSend.setOnClickListener {
                val text = binding.etComment.string()
               channelViewModel.addComment(tokenManager.getAccessToken(), CommentData(channelPostId, text, parentCommentId),
                    onSuccess = {
                        binding.etComment.setText("")
                        binding.llReplyInfo.hide()
                        parentCommentId = null
                        getChannelsPostComments()
                    },
                    onError = {})
            }
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    @SuppressLint("SetTextI18n")
    fun loadParentCommentIdAndName(parentId:Long, username:String){
        binding.llReplyInfo.show()
        binding.tvWhom.text = username
        parentCommentId  = parentId
        binding.ibDeleteReply.setOnClickListener {
            binding.llReplyInfo.hide()
            parentCommentId = null
        }
    }


  fun getChannelsPostComments(){
        channelViewModel.getComments(channelPostId, onSuccess = {binding.swipeRefreshLayout.isRefreshing=false}, onError = {})
    }

   fun getUserPostComments(){
        postViewModel.getComments(postId, onSuccess = {binding.swipeRefreshLayout.isRefreshing=false}, onError = {})
    }
}
