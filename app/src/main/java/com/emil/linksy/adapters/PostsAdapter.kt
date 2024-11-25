package com.emil.linksy.adapters

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.PostResponse
import com.emil.linksy.presentation.ui.ActionDialog
import com.emil.linksy.presentation.viewmodel.PostViewModel
import com.emil.linksy.util.TokenManager
import com.emil.presentation.R
import com.google.android.material.textview.MaterialTextView
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject

class PostsAdapter(private val postList: List<PostResponse>, private val postViewModel: PostViewModel,
                   private val context:Context, private val tokenManager: TokenManager): RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    inner class PostsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val authorAvatarImageView = itemView.findViewById<ImageView>(R.id.iv_author_avatar)
        private val authorUsername = itemView.findViewById<MaterialTextView>(R.id.tv_author_username)
        private val publicationDate = itemView.findViewById<MaterialTextView>(R.id.tv_date)
        private val postText = itemView.findViewById<MaterialTextView>(R.id.tv_text_post_content)
        private val likeCount = itemView.findViewById<MaterialTextView>(R.id.tv_like_count)
        private val repostsCount = itemView.findViewById<MaterialTextView>(R.id.tv_reposts_count)
        private val editPostButton = itemView.findViewById<ImageButton>(R.id.ib_edit_post)
        var sharedPref: SharedPreferences =  context.getSharedPreferences("TokenData", Context.MODE_PRIVATE)
        fun bind(post: PostResponse) {
              if (post.authorAvatarUrl !="null"){
                  Glide.with(context)
                      .load(post.authorAvatarUrl)
                      .apply(RequestOptions.circleCropTransform())
                      .into(authorAvatarImageView)
              }
            authorUsername.text = post.authorUsername
            publicationDate.text = post.publishDate
            postText.text = post.text
            likeCount.text = post.likesCount.toString()
            repostsCount.text = post.repostsCount.toString()
            editPostButton.setOnClickListener {
                val popupMenu = PopupMenu(context, editPostButton)
                val menu = popupMenu.menu
                menu.add(0, 1, 0, context.getString(R.string.edit))
                menu.add(0, 2, 1, context.getString(R.string.delete))
                val menuItem = menu.findItem(2)
                val spannableTitle = SpannableString(menuItem.title)
                spannableTitle.setSpan(ForegroundColorSpan(Color.RED), 0, spannableTitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                menuItem.title = spannableTitle

                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        1 -> {

                            true
                        }

                        2 -> {
                            val dialog = ActionDialog(context)
                            dialog.setTitle(context.getString(R.string.delete_post_title))
                            dialog.setAction {
                                val token = tokenManager.getAccessToken()
                                postViewModel.deletePost(token,post.postId, onSuccess ={dialog.dismiss()}, onError = {})
                            }
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_post, parent, false)
        return PostsViewHolder(view)
    }

    override fun getItemCount(): Int = postList.size


    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        holder.bind (postList[position])
    }

}