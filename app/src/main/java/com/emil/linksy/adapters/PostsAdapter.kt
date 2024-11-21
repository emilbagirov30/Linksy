package com.emil.linksy.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.PostResponse
import com.emil.presentation.R
import com.google.android.material.textview.MaterialTextView

class PostsAdapter(private val postList: List<PostResponse>, private val context:Context): RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {
    inner class PostsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val authorAvatarImageView = itemView.findViewById<ImageView>(R.id.iv_author_avatar)
        private val authorUsername = itemView.findViewById<MaterialTextView>(R.id.tv_author_username)
        private val publicationDate = itemView.findViewById<MaterialTextView>(R.id.tv_date)
        private val postText = itemView.findViewById<MaterialTextView>(R.id.tv_text_post_content)
        private val likeCount = itemView.findViewById<MaterialTextView>(R.id.tv_like_count)
        private val repostsCount = itemView.findViewById<MaterialTextView>(R.id.tv_reposts_count)
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