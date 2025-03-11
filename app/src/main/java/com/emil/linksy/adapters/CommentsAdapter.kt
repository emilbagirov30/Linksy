package com.emil.linksy.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.CommentResponse
import com.emil.linksy.presentation.ui.ActionDialog
import com.emil.linksy.presentation.ui.navigation.profile.CommentsBottomSheet
import com.emil.linksy.presentation.ui.page.UserPageActivity
import com.emil.linksy.presentation.viewmodel.ChannelViewModel
import com.emil.linksy.presentation.viewmodel.PostViewModel
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.emil.presentation.databinding.RvItemCommentsBinding

class CommentsAdapter (private val userId:Long, private val independentCommentList:List<CommentResponse>,
                       private val allCommentList:List<CommentResponse> = emptyList(),
                       private val commentsBottomSheet: CommentsBottomSheet? = null,
                       private val postViewModel: PostViewModel?=null,
                       private val channelViewModel: ChannelViewModel? = null,
                       private val tokenManager: TokenManager): RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>(){


    inner class CommentViewHolder(private val binding: RvItemCommentsBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context
        @SuppressLint("SetTextI18n")
        fun bind (comment:CommentResponse){
            binding.ivAvatar.setBackgroundResource(R.drawable.default_avatar)
            binding.tvReply.hide()
            binding.ivConfirmed.hide()
            binding.llViewResponses.hide()
            if (comment.authorAvatarUrl != Linksy.EMPTY_AVATAR) {
                Glide.with(context)
                    .load(comment.authorAvatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivAvatar)
            }
            if (userId!=comment.authorId) {
                binding.ivAvatar.setOnClickListener {
                    val switchingToUserPageActivity = Intent(context, UserPageActivity::class.java)
                    switchingToUserPageActivity.putExtra(Linksy.INTENT_USER_ID_KEY, comment.authorId)
                    context.startActivity(switchingToUserPageActivity)
                }
            }
            if (comment.confirmed) binding.ivConfirmed.show()
            binding.tvName.text = comment.authorName
            binding.tvComment.text = comment.commentText
            binding.tvDate.text = comment.date
            if (comment.parentCommentId==null) {
                binding.tvReply.setOnClickListener {
                    it.anim()
                    commentsBottomSheet?.loadParentCommentIdAndName(comment.commentId,comment.authorName)
                }
                val childComments = allCommentList.filter { c -> c.parentCommentId == comment.commentId }
                if (childComments.isNotEmpty()) {
                    binding.llViewResponses.show()
                    binding.tvResponses.text = "${context.getString(R.string.view_responses)}(${childComments.size})"
                }

                binding.llViewResponses.setOnClickListener {
                    it.anim()
                    binding.rvResponses.show()
                    binding.rvResponses.layoutManager = LinearLayoutManager(context)
                    binding.rvResponses.adapter = CommentsAdapter(userId,independentCommentList = childComments,
                        tokenManager = tokenManager, channelViewModel = channelViewModel, postViewModel = postViewModel)
                    it.hide()
                }
            }

            if (comment.authorId == userId){
                binding.tvDelete.show()
                binding.tvDelete.setOnClickListener {
                    val dialog = ActionDialog(context)
                    dialog.setTitle(context.getString(R.string.delete_comment_title))
                    dialog.setConfirmText(context.getString(R.string.delete_comment_confirm_text))
                    dialog.setAction {
                        channelViewModel?.deleteComment(tokenManager.getAccessToken(),comment.commentId, onSuccess = {
                            commentsBottomSheet?.getChannelsPostComments()
                            dialog.dismiss() })
                        postViewModel?.deleteComment(tokenManager.getAccessToken(),comment.commentId, onSuccess = {
                            commentsBottomSheet?.getUserPostComments()
                            dialog.dismiss()})
                    }
                }
            }else binding.tvDelete.hide()

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = RvItemCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun getItemCount(): Int  = independentCommentList.size

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(independentCommentList[position])
    }
}